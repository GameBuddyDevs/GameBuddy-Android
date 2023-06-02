package com.example.gamebuddy.presentation.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.GetAllFriendsUseCase
import com.example.gamebuddy.domain.usecase.main.removeFriendsUseCase
import com.example.gamebuddy.presentation.main.home.HomeEvent
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AllFriendsViewModel@Inject constructor(
    private val allFriendsUseCase: GetAllFriendsUseCase,
    private val removeFriendsUseCase: removeFriendsUseCase,
) : ViewModel()  {
    private val _usersUiState: MutableLiveData<AllFriendsState> =
        MutableLiveData(AllFriendsState())
    val usersUiState: MutableLiveData<AllFriendsState> get() = _usersUiState

    fun onTriggerEvent(event:AllFriendsEvent){
        when(event){
            is AllFriendsEvent.OnSetUserId -> onSetUserId(userId = event.userId)
            AllFriendsEvent.GetAllFriends -> getAllFriends()
            AllFriendsEvent.RemoveFriend -> removeFriends()
            AllFriendsEvent.ResetAllFriends -> resetAllFriends()
            AllFriendsEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
        }
    }
    private fun removeFriends(){
        val userId = _usersUiState.value?.userId
        removeFriendsUseCase.execute(userId)
            .onEach { dataState ->
                Timber.d("AllFriendsViewModelAcceptFriends: ${dataState.data}")
                dataState?.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
    }
    private fun resetAllFriends(){
        val userId = _usersUiState.value?.userId
        usersUiState.value?.resetAllFriends(userId!!)
    }
    private fun onSetUserId(userId: String){
        _usersUiState.value?.let { state ->
            _usersUiState.value = state.copy(userId = userId)
        }
    }
    private fun getAllFriends(){
        allFriendsUseCase.execute().onEach { dataState ->
            _usersUiState.value = usersUiState.value?.copy(isLoading = dataState.isLoading)

            dataState.data?.let { allFriends ->
                if (allFriends.isNotEmpty()){
                    Timber.d("getAllFriends : ${allFriends[0].username}")
                    _usersUiState.value = usersUiState.value?.copy(allFriends = allFriends)
                }
            }
            dataState.stateMessage?.let { stateMessage ->
                appendToMessageQueue(stateMessage)
            }
        }.launchIn(viewModelScope)
    }
    private fun removeHeadFromQueue() {
        _usersUiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                _usersUiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $_usersUiState")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }
    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _usersUiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    _usersUiState.value = state.copy(queue = queue)
                }
            }
        }
    }
}