package com.example.gamebuddy.presentation.main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableLiveData<MessageState> = MutableLiveData(MessageState())
    val uiState: MutableLiveData<MessageState> get() = _uiState

    fun onTriggerEvent(event: MessageEvent) {
        when (event) {
            is MessageEvent.NewQuery -> {
                newQuery()
            }
            is MessageEvent.UpdateQuery -> {
                updateQuery(event.query)
            }
            is MessageEvent.Error -> {
                appendToMessageQueue(event.stateMessage)
            }
            is MessageEvent.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
        }
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

    private fun appendToMessageQueue(stateMessage: StateMessage){
        _uiState.value?.let { state ->
            val queue = state.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(stateMessage.response.uiComponentType !is UIComponentType.None){
                    queue.add(stateMessage)
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

}