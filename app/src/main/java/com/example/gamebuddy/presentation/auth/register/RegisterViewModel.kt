package com.example.gamebuddy.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.RegisterUseCase
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.session.SessionManager
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    val state: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState())
    //val state: StateFlow<RegisterState> = _state

    fun onTriggerEvent(event: RegisterEvent) {
        when (event) {
            RegisterEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
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

    private fun removeHeadFromQueue() {
        state.value.let {
            try {
                val queue = it.queue
                queue.remove()
                state.value = it.copy(queue = queue)
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            registerUseCase.execute(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
            ).collect {dataState ->
                Timber.d("RegisterViewModel: ${dataState.stateMessage?.response}")
                Timber.d("RegisterViewModel queue count: ${state.value.queue.count()}")
                val queue = state.value.queue
                dataState.stateMessage?.let {
                    Timber.d("Added ${it.response.message}")
                    queue.add(it)
                }

                state.value = state.value.copy(
                    isLoading = dataState.isLoading,
                    queue = queue
                )
            }

        }
    }

//    private fun register(email: String, password: String, confirmPassword: String) {
//        state.value?.let { state ->
//            registerUseCase.execute(
//                email = email,
//                password = password,
//                confirmPassword = confirmPassword,
//            ).onEach { dataState ->
//                //_state.value = state.copy(isLoading = dataState.isLoading)
//                _state.value = state.copy(isLoading = true)
//
//                dataState.data?.let { authToken ->
//                    sessionManager.onTriggerEvent(
//                        SessionEvents.Login(
//                           authToken = authToken
//                        )
//                    )
//                }
//
//                dataState.stateMessage?.let { stateMessage ->
//                    Timber.d("RegisterViewModel about to add something to the queue ${stateMessage.response.message}")
//                    appendToMessageQueue(stateMessage)
//                }
//                Timber.d("RegisterViewModel: ${state.queue.count()}")
//
//                //_state.value = state.copy(queue = state.queue)
//
//
//            }.launchIn(viewModelScope)
//        }
//    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        state.value.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("RegisterViewModel Something added to queue: ${state.queue}")
                    this.state.value = state.copy(queue = queue)
                }
            }
        }
    }
}