package com.example.gamebuddy.presentation.main.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.PendingFriendUseCase
import com.example.gamebuddy.domain.usecase.main.acceptFriendsUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pendingFriendUseCase: PendingFriendUseCase,
    private val acceptFriendsUseCase: acceptFriendsUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<HomeState> = MutableLiveData(HomeState())
    val uiState: MutableLiveData<HomeState> get() = _uiState

    init {
        onTriggerEvent(HomeEvent.GetPendingFriends)
    }

    fun onTriggerEvent(event: HomeEvent) {
        when(event){
            is HomeEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
            is HomeEvent.OnSetUserId -> OnSetUserId(userId = event.userId)
            is HomeEvent.OnSetAcceptFriend -> OnSetAcceptFriend(accept = event.accept)
            HomeEvent.GetPendingFriends -> getPendingFriends()
            HomeEvent.AcceptFriends -> AcceptFriends()
            HomeEvent.ResetPendingFriends -> resetPendingFriends()
        }
    }
    private fun resetPendingFriends(){
        val userId = _uiState.value?.userId
        uiState.value?.resetPendingFriends(userId!!)
    }
    private fun OnSetUserId(userId:String){
        _uiState.value?.let { state ->
            _uiState.value = state.copy(userId = userId)
        }
    }
    private fun OnSetAcceptFriend(accept: Boolean){
        _uiState.value?.let { state ->
            _uiState.value = state.copy(accept = accept)
        }
    }
    private fun AcceptFriends(){
        val userId = _uiState.value?.userId
        val accept = _uiState.value?.accept
        acceptFriendsUseCase.execute(userId,accept)
            .onEach { dataState ->
                Timber.d("HomeViewModelAcceptFriends: ${dataState.data}")
                dataState?.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
    }
    private fun getPendingFriends() {
        pendingFriendUseCase.execute().onEach { dataState ->
            _uiState.value = uiState.value?.copy(isLoading = dataState.isLoading)

            dataState.data?.let { pendingFriends ->
                if(pendingFriends.isNotEmpty()){
                    Timber.d("getPendingFriends : ${pendingFriends[0].username}")
                    _uiState.value = uiState.value?.copy(pendingFriends = pendingFriends)
                }

            }
            dataState.stateMessage?.let { stateMessage ->
                appendToMessageQueue(stateMessage)
            }
        }.launchIn(viewModelScope)
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