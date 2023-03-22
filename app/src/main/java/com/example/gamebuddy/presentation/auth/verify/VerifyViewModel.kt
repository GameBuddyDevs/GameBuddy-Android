package com.example.gamebuddy.presentation.auth.verify

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.VerifyUseCase
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.session.SessionManager
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(
    private val verifyUseCase: VerifyUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _uiState: MutableLiveData<VerifyState> = MutableLiveData(VerifyState())
    val uiState: MutableLiveData<VerifyState> get() = _uiState

    fun onTriggerEvent(event: VerifyEvent) {
        when (event) {
            VerifyEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
            is VerifyEvent.EnteredVerificationCode -> approveAccount(event.verificationCode)
        }
    }

    private fun approveAccount(verificationCode: String) {
        _uiState.value.let { state ->
            verifyUseCase.execute(verificationCode = verificationCode)
                .onEach { datastate ->
                    _uiState.value = state?.copy(isLoading = datastate.isLoading)

                    datastate.data?.let { authToken ->
                        sessionManager.onTriggerEvent(SessionEvents.Login(authToken = authToken))
                    }

                    datastate.stateMessage?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun removeHeadFromQueue() {
        uiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                uiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $uiState")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("RegisterViewModel Something added to queue: ${state.queue}")
                    uiState.value = state.copy(queue = queue)
                }
            }
        }
    }
}