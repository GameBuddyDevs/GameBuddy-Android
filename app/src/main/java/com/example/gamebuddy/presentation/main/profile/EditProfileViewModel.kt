package com.example.gamebuddy.presentation.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.auth.EditUseCase
import com.example.gamebuddy.domain.usecase.auth.GamesUseCase
import com.example.gamebuddy.domain.usecase.auth.KeywordsUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val gamesUseCase: GamesUseCase,
    private val editUseCase: EditUseCase,
    private val keywordsUseCase: KeywordsUseCase,
) : ViewModel()  {
    private val _uiState:MutableLiveData<EditState> =
        MutableLiveData(EditState())
    val uiState: MutableLiveData<EditState> get() = _uiState

    fun onTriggerEvent(event:EditEvent){
        when(event){
            is EditEvent.OnSetAge -> onSetAge(age = event.age)
            is EditEvent.OnSetParam -> onSetParam(param = event.param)
            is EditEvent.OnSetUsername -> onSetUsername(username = event.username)
            is EditEvent.AddKeywordToSelected -> addKeywordToSelected(id=event.id)
            is EditEvent.AddGameToSelected -> addGameToSelected(id = event.id)
            EditEvent.GetGames -> getGames()
            EditEvent.GetKeywords -> getKeywords()
            EditEvent.Edit -> edit()
            EditEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()

        }
    }
    private fun getKeywords() {
        _uiState.value?.let { state->
            keywordsUseCase.execute().onEach { dataState ->
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
                _uiState.value = state?.copy(keywords = dataState.data?.body?.data?.keywords)
            }.launchIn(viewModelScope)
        }
    }
    private fun getGames() {
        _uiState.value?.let { state ->
            gamesUseCase.execute().onEach { dataState ->
                Timber.d("Edit Profile View Model getGames: ${dataState.data}")
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
                _uiState.value = state.copy(games = dataState.data)
            }.launchIn(viewModelScope)
        }
    }

    private fun onSetParam(param: String) {
        _uiState.value?.let { state ->
            _uiState.value = state.copy(param = param)
        }
    }

    private fun edit(){
        val param = _uiState.value?.param ?: ""
        val age = _uiState.value?.age?.toIntOrNull() ?: 0
        val username = _uiState.value?.username ?: ""
        val selectedGames = _uiState.value?.selectedGames ?: listOf()
        val selectedKeywords = _uiState.value?.selectedKeywords ?: listOf()

        editUseCase.execute(param,username,age,selectedGames,selectedKeywords)
            .onEach { dataState ->
                Timber.d("startup-logic: Collecting data: $dataState")
                _uiState.value = uiState.value?.copy(isLoading = dataState.isLoading)
                dataState?.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

                dataState?.data?.let { data ->
                    _uiState.value = uiState.value?.copy(isProfileSetupDone = true)
                }
            }.launchIn(viewModelScope)
    }
    private fun removeHeadFromQueue() {
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

    private fun addGameToSelected(id: String) {
        Timber.d(" Edit Profile view model addGameToSelected $id")
        _uiState.value?.let { state ->
            _uiState.value = state.copy(selectedGames = state.selectedGames + id)
            Timber.d("Selected games: ${_uiState.value!!.selectedGames}")
        }
    }

    private fun addKeywordToSelected(id: String) {
        _uiState.value?.let { state ->
            _uiState.value = state.copy(selectedKeywords = state.selectedKeywords + id)
            Timber.d("Selected keywords: ${_uiState.value!!.selectedKeywords}")

        }
    }

    private fun onSetUsername(username: String) {
        _uiState.value?.let { state ->
            _uiState.value = state.copy(username = username)
        }
    }

    private fun onSetAge(age: String) {
        _uiState.value?.let { state->
            _uiState.value = state.copy(age = age)
        }
    }
    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("EditProfileViewModel Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }
}