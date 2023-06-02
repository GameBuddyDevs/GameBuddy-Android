package com.example.gamebuddy.presentation.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.ProfileUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val ProfileUseCase: ProfileUseCase,
) :ViewModel() {
    private val _usersUiState: MutableLiveData<ProfileState> =
        MutableLiveData(ProfileState())
    val usersUiState: MutableLiveData<ProfileState> get() = _usersUiState

    fun onTriggerEvent(event:ProfileEvent){
        when(event){
            ProfileEvent.GetUserInfo -> getUserInfo()
            ProfileEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
        }
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
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("Profile ViewModel Something added to queue: ${state.queue}")
                    _usersUiState.value = state.copy(queue = queue)
                }
            }
        }
    }
    private fun getUserInfo(){
        _usersUiState.value?.let { state ->
            ProfileUseCase.execute().onEach { dataState ->
                Timber.d("Profile View Model Get User: ${dataState.data}")
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
                _usersUiState.value = state.copy(profileUser = dataState.data)
            }.launchIn(viewModelScope)
        }
    }
}