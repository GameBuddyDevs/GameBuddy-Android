package com.example.gamebuddy.presentation.main.achievement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamebuddy.domain.model.achievement.Achievement
import com.example.gamebuddy.domain.usecase.main.AchievementUseCase
import com.example.gamebuddy.domain.usecase.main.CollectAchievementUseCase
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
    private val collectAchievementUseCase: CollectAchievementUseCase
) : ViewModel() {

    private val _uiState: MutableLiveData<AchievementState> = MutableLiveData(AchievementState())
    val uiState: MutableLiveData<AchievementState> get() = _uiState

    init {
        onTriggerEvent(AchievementEvent.GetAchievement)
    }

    fun onTriggerEvent(event: AchievementEvent) {
        when (event) {
            AchievementEvent.GetAchievement -> getAchievements()
            is AchievementEvent.CollectAchievement -> collectAchievement(event.achievementId)
            AchievementEvent.OnRemoveHeadFromQueue -> removeHeadFromQueue()
        }
    }

    private fun collectAchievement(achievementId: String) {
        _uiState.value?.let { state ->
            collectAchievementUseCase.execute(achievementId).onEach { dataState ->
                Timber.d("AchievementViewModel: $dataState")
                _uiState.value = state.copy(isLoading = dataState.isLoading)

//                dataState.data?.let { achievement ->
//                    _uiState.value = state.copy(achievements = achievements)
//                }

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getAchievements() {
        _uiState.value?.let { state ->
            achievementUseCase.execute().onEach { dataState ->
                Timber.d("Achievement View Model getAchievements: ${dataState.data}")

                val earnedAchievements = mutableListOf<Achievement>()

                dataState.data?.forEach { achievement ->
                    earnedAchievements.add(achievement)
                }

                _uiState.value = state.copy(achievements = earnedAchievements)

                dataState.stateMessage?.let { stateMessage ->
                    appendToMessageQueue(stateMessage)
                }
            }.launchIn(viewModelScope)
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

}