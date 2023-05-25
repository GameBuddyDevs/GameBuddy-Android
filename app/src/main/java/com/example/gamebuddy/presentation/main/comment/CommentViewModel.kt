package com.example.gamebuddy.presentation.main.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.comments.CreateCommentUseCase
import com.example.gamebuddy.domain.usecase.comments.GetCommentsUseCase
import com.example.gamebuddy.domain.usecase.comments.LikeCommentUseCase
import com.example.gamebuddy.domain.usecase.community.LikePostUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val getCommentsUseCase: GetCommentsUseCase,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val savedStatedHandle: SavedStateHandle
) : ViewModel() {


    private val _uiState: MutableLiveData<CommentState> = MutableLiveData(CommentState())
    val uiState: MutableLiveData<CommentState> get() = _uiState

    init {
        savedStatedHandle.get<String>("postId")?.let { postId ->
            onTriggerEvent(CommentEvent.GetComments(postId))
        }
    }

    fun onTriggerEvent(event: CommentEvent) {
        when (event) {
            is CommentEvent.GetComments -> getComments(event.postId)
            is CommentEvent.LikeComment -> likeComment(event.commentId)
            CommentEvent.LikeCurrentPost -> likeCurrentPost()
            is CommentEvent.CreateComment -> createComment(event.comment)
            CommentEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun createComment(comment: String) {
        savedStatedHandle.get<String>("postId")?.let { postId ->
            _uiState.value?.let { state ->
                createCommentUseCase.execute(postId, comment).onEach { dataState ->
                    _uiState.value = state.copy(isLoading = dataState.isLoading)

                    dataState.data?.let { comment ->
                        _uiState.value = state.copy(comments = state.comments?.plus(comment))
                    }

                    dataState.stateMessage?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }

                }.launchIn(viewModelScope)
            }
        }
    }

    private fun likeCurrentPost() {
        _uiState.value?.let { state ->
            savedStatedHandle.get<String>("postId")?.let { postId ->
                likePostUseCase.execute(postId).onEach { dataState ->

                    _uiState.value = state.copy(isLoading = dataState.isLoading)

                    dataState.stateMessage?.let { stateMessage ->
                        appendToMessageQueue(stateMessage)
                    }

                }.launchIn(viewModelScope)
            }
        }
    }


    private fun getComments(postId: String) {
        _uiState.value?.let { state ->
            getCommentsUseCase.execute(postId).onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { comments ->
                    _uiState.value = state.copy(comments = comments)
                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun likeComment(commentId: String) {
        _uiState.value?.let { state ->
            likeCommentUseCase.execute(commentId).onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
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