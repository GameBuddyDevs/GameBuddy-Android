package com.example.gamebuddy.presentation.main.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val savedStatedHandle: SavedStateHandle
) {

    //TODO: Implement SavedStateHandle
    //TODO: Add adapter for comments

    private val _uiState: MutableLiveData<CommentState> = MutableLiveData(CommentState())
    val uiState: MutableLiveData<CommentState> get() = _uiState

    fun onTriggerEvent(event: CommentEvent) {
        when (event) {
            is CommentEvent.GetComments -> getComments(event.postId)
            CommentEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun getComments(postId: String) {
        TODO("Not yet implemented")
    }

    private fun onRemoveHeadFromQueue() {
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