package com.example.gamebuddy.presentation.auth.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.ForgotPasswordUseCase
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
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState: MutableLiveData<ForgotPasswordState> =
        MutableLiveData(ForgotPasswordState())
    val uiState: MutableLiveData<ForgotPasswordState> get() = _uiState

    fun onTriggerEvent(event: ForgotPasswordEvent) {
        when (event) {
            ForgotPasswordEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
            is ForgotPasswordEvent.OnUpdateEmail -> TODO()
            is ForgotPasswordEvent.ForgotPassword -> {
                forgotPassword(
                    email = event.email,
                    isRegister = event.isRegister
                )
            }
        }
    }

    private fun removeHeadFromQueue() {
        _uiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                _uiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $uiState")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("ForgotPassword View Model Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun forgotPassword(
        email: String,
        isRegister: Boolean,
    ) {
        uiState.value?.let { state ->
            forgotPasswordUseCase.execute(
                email = email,
                isRegister = isRegister,
            ).onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
                if (dataState.data != null) {
                    _uiState.value = state.copy(isForgotPasswordCompleted = true)
                }
            }.launchIn(viewModelScope)

        }
    }
}