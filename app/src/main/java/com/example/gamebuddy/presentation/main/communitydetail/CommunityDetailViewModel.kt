package com.example.gamebuddy.presentation.main.communitydetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.community.GetPostFromCommunityUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityDetailViewModel @Inject constructor(
    private val getPostFromCommunityUseCase: GetPostFromCommunityUseCase
): ViewModel() {

    private val _uiState: MutableLiveData<CommunityDetailState> = MutableLiveData(CommunityDetailState())
    val uiState: MutableLiveData<CommunityDetailState> get() = _uiState

    fun onTriggerEvent(event: CommunityDetailEvent) {
        when (event) {
            is CommunityDetailEvent.GetPosts -> getPosts(event.communityId)
            CommunityDetailEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun getPosts(communityId: String) {
        _uiState.value?.let { state ->
            getPostFromCommunityUseCase.execute(communityId).onEach { dataState ->
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