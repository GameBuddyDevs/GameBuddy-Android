package com.example.gamebuddy.presentation.auth.username

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.UsernameUseCase
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
class UsernameViewModel @Inject constructor(
    private val usernameUseCase: UsernameUseCase,
    private val sessionManager: SessionManager,
) : ViewModel() {
    private val _uiState: MutableLiveData<UsernameState> = MutableLiveData(UsernameState())
    val uiState: MutableLiveData<UsernameState> get() = _uiState

    fun onTriggerEvent(event: UsernameEvent) {
        when (event) {
            UsernameEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
            is UsernameEvent.OnUpdateUsername -> TODO()
            is UsernameEvent.Username -> {
                username(
                    username = event.username,
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
                Timber.d("Queue count after remove head: $_uiState")
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
                    Timber.d("UsernameViewModel Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }
    private fun username(
        username: String,
    ) {
        _uiState.value?.let { state ->
            usernameUseCase.execute(
                username = username,
            ).onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
                if (dataState.data != null){
                    _uiState.value = state.copy(isUsernameCompleted = true)
                }
            }.launchIn(viewModelScope)
        }
    }
}