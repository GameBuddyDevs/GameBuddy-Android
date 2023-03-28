package com.example.gamebuddy.presentation.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.LoginUseCase
import com.example.gamebuddy.session.SessionEvents
import com.example.gamebuddy.session.SessionManager
import com.example.gamebuddy.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val sessionManager: SessionManager,
): ViewModel(){
    val uiState : MutableLiveData<LoginState> = MutableLiveData(LoginState())

    fun OnTriggerEvent(event: LoginEvent){
        when(event){
            LoginEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
            is LoginEvent.OnUpdateEmail -> TODO()
            is LoginEvent.OnUpdatePassword -> TODO()
            is LoginEvent.Login -> {
                login(
                    email = event.email,
                    password = event.password,
                )
            }
        }
    }

    private fun removeHeadFromQueue() {
        uiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                this.uiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $uiState")
            }catch (e: Exception){
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage:StateMessage){
        uiState.value?.let { state ->
            val queue = state.queue
            if(!stateMessage.isMessageExistInQueue(queue)){
                if(stateMessage.response.uiComponentType !is UIComponentType.None){
                    queue.add(stateMessage)
                    Timber.d("LoginViewModel Something added to queue: ${state.queue}")
                    this.uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun login(
        email: String,
        password: String,
    ) {
        uiState.value?.let { state ->
            loginUseCase.execute(
                email = email,
                password = password,
            ).onEach { dataState ->
                this.uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { authToken ->
                    sessionManager.onTriggerEvent(SessionEvents.Login(authToken))
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }

    }


}