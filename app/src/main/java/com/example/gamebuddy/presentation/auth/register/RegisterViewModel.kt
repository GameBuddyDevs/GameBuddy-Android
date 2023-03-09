package com.example.gamebuddy.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.RegisterUseCase
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.session.SessionManager
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    val state: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())

    fun onTriggerEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.OnRemoveHeadFromQueue -> TODO()
            is RegisterEvent.OnUpdateConfirmPassword -> TODO()
            is RegisterEvent.OnUpdateEmail -> TODO()
            is RegisterEvent.OnUpdatePassword -> TODO()
            is RegisterEvent.Register -> {
                register(
                    email = event.email,
                    password = event.password,
                    confirmPassword = event.confirmPassword,
                )
            }
        }
    }

    private fun register(email: String, password: String, confirmPassword: String) {
        state.value?.let { state ->
            registerUseCase.execute(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
            ).onEach { dataState ->
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { authToken ->
                    sessionManager.onTriggerEvent(
                        SessionEvents.Login(
                           authToken = authToken
                        )
                    )
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        state.value.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType is UIComponentType.None) {
                    queue.add(stateMessage)
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }
}