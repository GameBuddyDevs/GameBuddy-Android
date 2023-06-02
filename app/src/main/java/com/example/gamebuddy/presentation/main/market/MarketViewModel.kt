package com.example.gamebuddy.presentation.main.market

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.usecase.main.BuyItemUseCase
import com.example.gamebuddy.domain.usecase.main.CoinUsecase
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
    private val marketUseCase: MarketUseCase,
    private val coinUseCase: CoinUsecase,
    private val buyItemUseCase: BuyItemUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<MarketState> = MutableLiveData(MarketState())
    val uiState: MutableLiveData<MarketState> get() = _uiState

    fun onTriggerEvent(event: MarketEvent) {
        when (event) {
            is MarketEvent.OnSetAvatarId -> OnSetAvatarId(avatarId = event.avatarId)
            MarketEvent.GetAvatars -> getAvatars()
            MarketEvent.GetCoin -> getCoin()
            MarketEvent.BuyItem -> buyItem()
            MarketEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
        }
    }
    private fun buyItem() {
        val avatarId = _uiState.value?.avatarId
        buyItemUseCase.execute(avatarId)
            .onEach { dataState ->
                Timber.d("MarketViewModel Buy Item: ${dataState.data}")
                dataState?.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
    }

    private fun OnSetAvatarId(avatarId:String){
        _uiState.value?.let { state->
            _uiState.value = state.copy(avatarId = avatarId)
        }
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

    private fun appendToMessageQueue(stateMessage: StateMessage) {
        _uiState.value?.let { state ->
            val queue = state.queue
            if (!stateMessage.isMessageExistInQueue(queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    Timber.d("MarketViewModel Something added to queue: ${state.queue}")
                     _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }
    private fun getCoin() {
        coinUseCase.execute().onEach { dataState ->
            _uiState.value = uiState.value?.copy(isLoading = dataState.isLoading)
            dataState.data?.let { data ->
                _uiState.value = uiState.value?.copy(coin = data)
            }
            dataState.stateMessage?.let { stateMessage ->
                appendToMessageQueue(stateMessage)
            }
        }.launchIn(viewModelScope)
    }
    private fun getAvatars() {
        marketUseCase.execute().onEach { dataState ->
            _uiState.value = uiState.value?.copy(isLoading = dataState.isLoading)
            dataState.data?.let { avatars->
                _uiState.value = uiState.value?.copy(avatars = avatars)
            }
            dataState.stateMessage?.let { stateMessage ->
                appendToMessageQueue(stateMessage)
            }
        }.launchIn(viewModelScope)
    }
}