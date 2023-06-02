package com.example.gamebuddy.presentation.auth.newpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.NewPasswordUseCase
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
class NewPasswordViewModel @Inject constructor(
    private val newPasswordUseCase: NewPasswordUseCase,
    private val sessionManager: SessionManager,
) : ViewModel()  {
    private val _uiState: MutableLiveData<NewPasswordState> = MutableLiveData(NewPasswordState())
    val uiState: MutableLiveData<NewPasswordState> get() = _uiState

    fun onTriggerEvent(event: NewPasswordEvent) {
        when (event) {
            NewPasswordEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
            is NewPasswordEvent.OnUpdatePassword -> TODO()
            is NewPasswordEvent.OnUpdateConfirmPassword -> TODO()
            is NewPasswordEvent.NewPassword -> {
                newPassword(
                    password = event.password,
                    confirmPassword = event.confirmPassword
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
                    Timber.d("NewPassword View Model Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }
    private fun newPassword(
        password: String,
        confirmPassword: String,
    ) {
        uiState.value?.let { state ->
            newPasswordUseCase.execute(
                password = password,
                confirmPassword = confirmPassword,
            ).onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

                if (dataState.data != null){
                    _uiState.value = state.copy(isNewPasswordCompleted = true)
                }
            }.launchIn(viewModelScope)
        }
    }
}