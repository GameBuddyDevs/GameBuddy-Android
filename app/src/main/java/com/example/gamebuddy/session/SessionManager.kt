package com.example.gamebuddy.session

import androidx.lifecycle.MutableLiveData
import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.domain.usecase.session.CheckPreviousAuthUserUseCase
import com.example.gamebuddy.util.Constants
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val checkPreviousAuthUser: CheckPreviousAuthUserUseCase,
    //private val logout: SessionEvents.Logout,
    private val appDataStore: AppDataStore,
) {

    private val sessionScope = CoroutineScope(Main)

    private val _sessionState: MutableLiveData<SessionState> = MutableLiveData(SessionState())
    val sessionState: MutableLiveData<SessionState> = _sessionState

    init {
        sessionScope.launch {
            appDataStore.getValue(Constants.LAST_AUTH_USER)?.let { email ->
                onTriggerEvent(SessionEvents.CheckPreviousAuthUser(email))
            } ?: onFinishedCheckingForPreviousAuthUser()
        }
    }

    private fun onFinishedCheckingForPreviousAuthUser() {
        _sessionState.value.let { state ->
            _sessionState.value = state?.copy(didCheckForPreviousAuthUser = true)
        }
    }


    fun onTriggerEvent(event: SessionEvents) {
        when (event) {
            is SessionEvents.CheckPreviousAuthUser -> checkPreviousAuthUser(email = event.email)
            is SessionEvents.Login -> {
                login(event.authToken)
            }
            SessionEvents.Logout -> TODO()
            SessionEvents.OnRemoveHeadFromQueue -> TODO()
            is SessionEvents.Register -> {

            }
        }
    }

    private fun checkPreviousAuthUser(email: String) {
        _sessionState.value.let { state ->
            checkPreviousAuthUser.execute(email = email).onEach { dataState ->
                _sessionState.value = state?.copy(isLoading = dataState.isLoading)
                dataState.data?.let { authToken ->
                    _sessionState.value = state?.copy(authToken = authToken)
                    onTriggerEvent(SessionEvents.Login(authToken = authToken))
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Done checking for previously authenticated user.")
                        onFinishedCheckingForPreviousAuthUser()
                    else
                        appendToMessageQueue(stateMessage)
                }
            }.launchIn(sessionScope)
        }
    }

    private fun removeHeadFromQueue(){
        _sessionState.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                _sessionState.value = state.copy(queue = queue)
            }catch (e: Exception){
                Timber.d("Removed head from queue. Nothing to remove: ${e.message}.")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _sessionState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    _sessionState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun login(authToken: AuthToken) {
        _sessionState.value.let { state ->
            _sessionState.value = state?.copy(authToken = authToken)
        }
    }

}