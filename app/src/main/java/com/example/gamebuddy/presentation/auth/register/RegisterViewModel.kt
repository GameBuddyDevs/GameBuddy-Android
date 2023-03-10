package com.example.gamebuddy.presentation.auth.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.RegisterUseCase
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.session.SessionManager
import com.example.gamebuddy.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {

    val state: MutableLiveData<RegisterState> = MutableLiveData(RegisterState())

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
        state.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                this.state.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $state")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        state.value?.let { state ->
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

    private fun register(
        email: String,
        password: String,
        confirmPassword: String,
    ) {
        state.value?.let { state ->
            registerUseCase.execute(
                email = email,
                password = password,
                confirmPassword = confirmPassword,
            ).onEach { dataState ->
                this.state.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { authToken ->
                    sessionManager.onTriggerEvent(SessionEvents.Login(authToken))
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


//    private fun register(email: String, password: String, confirmPassword: String) {
//        viewModelScope.launch {
//            registerUseCase.execute(
//                email = email,
//                password = password,
//                confirmPassword = confirmPassword,
//            ).collect { dataState ->
//
//                when(dataState) {
//                    is success -> {
//                        Timber.d("RegisterViewModel Success: ${dataState.data}")
//                    }
//                }
//
//                //_uiState.update { it.copy(isLoading = dataState.isLoading) }
//
//                val queue = uiState.value.queue
//                dataState.stateMessage?.let {
//                    Timber.d("Added ${it.response.message}")
//                    queue.add(it)
//                }
//
//                _uiState.value = uiState.value.copy(
//                    isLoading = dataState.isLoading,
//                    queue = queue
//                )
//
////                dataState.stateMessage?.let { stateMessage ->
////                    appendToMessageQueue(stateMessage)
////                }
//
//                //_uiState.value = uiState.value.copy(isLoading = dataState.isLoading)
//                //_uiState.update { it.copy(isLoading = dataState.isLoading) }
//
//                dataState.data?.let { authToken ->
//                    sessionManager.onTriggerEvent(SessionEvents.Login(authToken = authToken))
//                }
//
//            }
//
//        }
//    }

}