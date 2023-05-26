package com.example.gamebuddy.presentation.main.community

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.community.GetPostsUseCase
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
class CommunityViewModel @Inject constructor(
    private val likePostUseCase: LikePostUseCase,
    private val getPostsUseCase: GetPostsUseCase,
) : ViewModel() {

    private val _uiState: MutableLiveData<CommunityState> = MutableLiveData(CommunityState())
    val uiState: MutableLiveData<CommunityState> get() = _uiState

    init {
        onTriggerEvent(CommunityEvent.GetPosts)
    }

    fun onTriggerEvent(event: CommunityEvent) {
        when (event) {
            is CommunityEvent.GetPosts -> getPosts()
            is CommunityEvent.LikePost -> likePost(event.postId)
            CommunityEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun likePost(postId: String) {
        _uiState.value?.let { state ->
            likePostUseCase.execute(postId).onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getPosts() {
        _uiState.value?.let { state ->
            getPostsUseCase.execute().onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { posts ->
                    _uiState.value = state.copy(posts = posts)
                }

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