package com.example.gamebuddy.presentation.main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.GetMessagesUseCase
import com.example.gamebuddy.domain.usecase.main.SendFriendRequestUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val sendFriendRequestUseCase: SendFriendRequestUseCase,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableLiveData<ChatState> = MutableLiveData(ChatState())
    val uiState: MutableLiveData<ChatState> get() = _uiState

    fun onTriggerEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.GetMessages -> getMessages(event.receiverId)
            is ChatEvent.SendMessage -> sendMessage(event.receiverId, event.message)
            is ChatEvent.AddFriend -> addFriend(event.matchedUserId)
            is ChatEvent.SetUserProperties -> setUserProperties(event.matchedUserId)
            ChatEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun setUserProperties(matchedUserId: String) {
        _uiState.value?.let { state ->
            _uiState.value = state.copy(matchedUserId = matchedUserId)
        }
    }

    private fun addFriend(matchedUserId: String?) {
        _uiState.value?.let { state ->
            if (matchedUserId != null) {
                sendFriendRequestUseCase.execute(matchedUserId)
                    .onEach { dataState ->
                        _uiState.value = state.copy(isLoading = dataState.isLoading)

                        dataState.data?.let {
                            _uiState.value = state.copy(isFriendRequestSend = true)
                        }

                        dataState.stateMessage?.let { stateMessage ->
                            appendToMessageQueue(stateMessage)
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun sendMessage(receiverId: String, message: String) {
        TODO("Not yet implemented")
    }

    private fun getMessages(receiverId: String) {
        _uiState.value?.let { state ->
            getMessagesUseCase.execute(receiverId)
                .onEach { dataState ->
                    _uiState.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let { messageData ->
                        _uiState.value = state.copy(messages = messageData.conversations)
                    }

                    dataState.stateMessage?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun onRemoveHeadFromQueue() {
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
                    Timber.d("LoginViewModel Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

}
