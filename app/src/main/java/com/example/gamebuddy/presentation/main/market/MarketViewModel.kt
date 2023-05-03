package com.example.gamebuddy.presentation.main.market

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.MarketUseCase
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val marketUseCase: MarketUseCase
) : ViewModel() {
    private val _uiState: MutableLiveData<MarketState> = MutableLiveData(MarketState())
    val uiState: MutableLiveData<MarketState> get() = _uiState

    fun onTriggerEvent(event: MarketEvent) {
        when (event) {
            is MarketEvent.UpdateQuery -> {
                updateQuery(event.query)
            }
            MarketEvent.NewQuery -> {
                newQuery()
            }
            MarketEvent.NewSearch -> {
                newSearch()
            }
            MarketEvent.OnRemoveHeadFromQueue -> {
                removeHeadFromQueue()
            }
            MarketEvent.GetAvatars -> {
                getAvatars()
            }

        }
    }
    private fun updateQuery(query: String) {
        TODO("Not yet implemented")
    }

    private fun newQuery() {
        TODO("Not yet implemented")
    }

    private fun newSearch() {
        TODO("Not yet implemented")
    }

    private fun removeHeadFromQueue() {
        _uiState.value?.let {
            try {
                val queue = it.queue
                queue.remove()
                //_uiState.value = it.copy(queue = queue)
                Timber.d("Queue count after remove head: $_uiState")
            } catch (e: Exception) {
                Timber.d("Nothing to remove ${e.message}")
            }
        }
    }

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("RegisterViewModel Something added to queue: ${state.queue}")
                   // _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun getAvatars() {
        _uiState.value?.let { state ->
            marketUseCase.execute().onEach { dataState ->
                Timber.d("Match View Model getUsers: ${dataState.data}")
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

               // _uiState.value = state.copy(avatars = dataState.data)
            }.launchIn(viewModelScope)
        }
    }


}