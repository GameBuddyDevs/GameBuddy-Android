package com.example.gamebuddy.presentation.main.populargames

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.GetPopularGamesUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.doesMessageAlreadyExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PopularGamesViewModel @Inject constructor(
    private val getPopularGamesUseCase: GetPopularGamesUseCase
): ViewModel() {

    private val _uiState: MutableLiveData<PopularGamesState> = MutableLiveData(PopularGamesState())
    val uiState: MutableLiveData<PopularGamesState> get() = _uiState

    init {
        onTriggerEvent(PopularGamesEvent.GetGames)
    }

    fun onTriggerEvent(event: PopularGamesEvent) {
        when (event) {
            PopularGamesEvent.GetGames -> getGames()
            PopularGamesEvent.OnRemoveHeadFromQueue -> onRemoveHeadFromQueue()
        }
    }

    private fun getGames() {
        _uiState.value?.let { state ->
            getPopularGamesUseCase.execute().onEach { dataState ->
                _uiState.value = state.copy(isLoading = dataState.isLoading)

                dataState.data?.let { data ->
                    _uiState.value = state.copy(games = data)
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