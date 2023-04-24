package com.example.gamebuddy.session

import androidx.lifecycle.MutableLiveData
import com.example.gamebuddy.data.datastore.AppDataStore
import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.domain.usecase.auth.ValidateTokenUseCase
import com.example.gamebuddy.domain.usecase.session.CheckPreviousAuthUserUseCase
import com.example.gamebuddy.util.AuthActionType
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
    private val validateTokenUseCase: ValidateTokenUseCase,
    private val appDataStore: AppDataStore,
) {

    private val sessionScope = CoroutineScope(Main)

    private val _sessionState: MutableLiveData<SessionState> = MutableLiveData(SessionState())
    val sessionState: MutableLiveData<SessionState> = _sessionState

    init {
        sessionScope.launch {
            appDataStore.getValue(Constants.LAST_AUTH_USER)?.let { email ->
                Timber.d("startup-logic: Found previous auth user: $email")
                onTriggerEvent(SessionEvents.CheckPreviousAuthUser(email = email))
            } ?: onUserNotFound()
        }
    }

    fun onTriggerEvent(event: SessionEvents) {
        when (event) {
            is SessionEvents.CheckPreviousAuthUser -> checkPreviousAuthUser(email = event.email)
            is SessionEvents.Login -> login(event.authToken)
            SessionEvents.Logout -> TODO()
            is SessionEvents.ValidateToken -> validateToken(event.authToken)
            SessionEvents.OnRemoveHeadFromQueue -> removeHeadFromQueue()
        }
    }

    private fun checkPreviousAuthUser(email: String) {
        _sessionState.value.let { state ->
            checkPreviousAuthUser.execute(email = email).onEach { dataState ->
                _sessionState.value = state?.copy(isLoading = dataState.isLoading)

                dataState.data?.let { authToken ->
                    Timber.d("startup-logic: Found previous auth user: $authToken")
                    _sessionState.value = state?.copy(authToken = authToken)
                    onTriggerEvent(SessionEvents.ValidateToken(authToken = authToken))
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Done checking for previously authenticated user.") {
                        onFinishedCheckingForPreviousAuthUser()
                    } else {
                        appendToMessageQueue(stateMessage)
                    }
                }

            }.launchIn(sessionScope)
        }
    }

    private fun onUserNotFound() {
        _sessionState.value.let { state ->
            Timber.d("startup-logic: User not registered")
            _sessionState.value = state?.copy(
                didCheckForPreviousAuthUser = true,
                actionType = AuthActionType.LOGIN
            )
        }
    }

    private fun onFinishedCheckingForPreviousAuthUser() {
        _sessionState.value.let { state ->
            Timber.d("startup-logic: Finished checking for previous auth user")
            _sessionState.value = state?.copy(didCheckForPreviousAuthUser = true)
        }
    }

    private fun validateToken(authToken: AuthToken) {
        _sessionState.value?.let { state ->
            validateTokenUseCase.execute(authToken.token!!)
                .onEach { dataState ->
                    _sessionState.value = state.copy(isLoading = dataState.isLoading)
                    dataState.data?.let { isVerified ->
                        if (isVerified) {
                            val isProfileSetupComplete =
                                appDataStore.getValue(Constants.PROFILE_COMPLETED) ?: "0"
                            _sessionState.value =
                                state.copy(actionType = if (isProfileSetupComplete == "1") AuthActionType.HOME else AuthActionType.DETAILS)
                        } else {
                            _sessionState.value = state.copy(actionType = AuthActionType.LOGIN)
                        }
                    }
                    dataState.stateMessage?.let { stateMessage ->
                        if (stateMessage.response.message == "Done validating token.") {
                            onFinishedCheckingForPreviousAuthUser()
                        } else {
                            appendToMessageQueue(stateMessage)
                        }
                    }
                }.launchIn(sessionScope)
        }
    }

    private fun removeHeadFromQueue() {
        _sessionState.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                _sessionState.value = state.copy(queue = queue)
            } catch (e: Exception) {
                Timber.d("startup-logic: Removed head from queue. Nothing to remove: ${e.message}.")
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
        _sessionState?.value.let { state ->
            _sessionState.value = state?.copy(authToken = authToken)
        }
    }

}