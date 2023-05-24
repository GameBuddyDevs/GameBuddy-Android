package com.example.gamebuddy.presentation.main.achievement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.model.achievement.Achievement
import com.example.gamebuddy.domain.usecase.main.AchievementUseCase
import com.example.gamebuddy.domain.usecase.main.MarketUseCase
import com.example.gamebuddy.presentation.main.market.MarketEvent
import com.example.gamebuddy.presentation.main.market.MarketState
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.UIComponentType
import com.example.gamebuddy.util.isMessageExistInQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val achievementUseCase: AchievementUseCase,
) : ViewModel() {
    private val _uiState: MutableLiveData<AchievementState> = MutableLiveData(AchievementState())
    val uiState: MutableLiveData<AchievementState> get() = _uiState

    fun onTriggerEvent(event: AchievementEvent) {
        when (event) {
            AchievementEvent.GetAchievement -> getAchievements()
            AchievementEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
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
                    Timber.d("AchievementViewModel Something added to queue: ${state.queue}")
                    _uiState.value = state.copy(queue = queue)
                }
            }
        }
    }

    private fun getAchievements() {
        _uiState.value?.let { state ->
            achievementUseCase.execute().onEach { dataState ->
                Timber.d("Achievement View Model getAchievements: ${dataState.data}")
                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }

                val earnedAchievements = mutableListOf<Achievement>()
                val unearnedAchievements = mutableListOf<Achievement>()
                val collectedAchievements = mutableListOf<Achievement>()

                dataState.data?.forEach { achievement ->
                    when {
                        achievement.isCollected -> collectedAchievements.add(achievement)
                        achievement.isEarned -> earnedAchievements.add(achievement)
                        else -> unearnedAchievements.add(achievement)
                    }
                }
                _uiState.value = state?.copy(
                    earnedAchievements = earnedAchievements,
                    unearnedAchievements = unearnedAchievements,
                    collectedAchievements = collectedAchievements
                )
            }.launchIn(viewModelScope)
        }
    }
}