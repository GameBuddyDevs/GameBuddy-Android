package com.example.gamebuddy.presentation.main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.GetChatBoxUseCase
import com.example.gamebuddy.domain.usecase.main.GetFriendsUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class ChatBoxViewModel @Inject constructor(
    private val getFriendsUseCase: GetFriendsUseCase,
    private val getChatBoxUseCase: GetChatBoxUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<ChatBoxState> = MutableLiveData(ChatBoxState())
    val uiState: MutableLiveData<ChatBoxState> get() = _uiState

    init {
//        viewModelScope.launch {
//            Timber.d("launch job...")
//            val time = measureTimeMillis {
//                val result1 = async { getFriends() }
//                val result2 = async { getChatBox() }
//                Timber.d("result1: ${result1.await()}")
//                Timber.d("result2: ${result2.await()}")
//            }
//            Timber.d("job completed in $time ms")
//        }

        onTriggerEvent(ChatBoxEvent.GetFriends)
        onTriggerEvent(ChatBoxEvent.GetChatBox)

    }


    fun onTriggerEvent(event: ChatBoxEvent) {
        when (event) {
            is ChatBoxEvent.NewQuery -> {
                newQuery()
            }

            is ChatBoxEvent.UpdateQuery -> {
                updateQuery(event.query)
            }

            is ChatBoxEvent.Error -> {
                appendToMessageQueue(event.stateMessage)
            }

            is ChatBoxEvent.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }

            ChatBoxEvent.GetChatBox -> getChatBox()

            ChatBoxEvent.GetFriends -> getFriends()

            ChatBoxEvent.NewSearch -> searchChatBox()
        }
    }

    private fun searchChatBox() {
        clearList()
        getChatBox()
    }

    private fun clearList() { _uiState.value = uiState.value?.copy(chatBox = listOf()) }


    private fun getChatBox() {
        getChatBoxUseCase.execute().onEach { dataState ->
            _uiState.value = uiState.value?.copy(isLoading = dataState.isLoading)

            dataState.data?.let { chatBox ->
                Timber.d("getFriends: friends: ${chatBox.size}")
                _uiState.value = uiState.value?.copy(chatBox = chatBox)
            }

            dataState.stateMessage?.let { stateMessage ->
                appendToMessageQueue(stateMessage)
            }
        }.launchIn(viewModelScope)
    }

    private fun getFriends() {
        getFriendsUseCase.execute().onEach { dataState ->
            _uiState.value = uiState.value?.copy(isLoading = dataState.isLoading)

            dataState.data?.let { friends ->
                Timber.d("getFriends: friends: ${friends.size}")
                _uiState.value = uiState.value?.copy(friends = friends)
            }

            dataState.stateMessage?.let { stateMessage ->
                appendToMessageQueue(stateMessage)
            }
        }.launchIn(viewModelScope)
    }

    private fun updateQuery(query: String) {
        TODO("Not yet implemented")
    }

    private fun newQuery() {
        TODO("Not yet implemented")
    }

    private fun removeHeadFromQueue() {
        _uiState.value?.let { state ->
            try {
                val queue = state.queue
                queue.remove() // can throw exception if empty
                _uiState.value = state.copy(queue = queue)
            } catch (e: Exception) {
                Timber.d("removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

}