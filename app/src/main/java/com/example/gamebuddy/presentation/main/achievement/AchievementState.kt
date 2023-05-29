package com.example.gamebuddy.presentation.main.achievement

import com.example.gamebuddy.domain.model.achievement.Achievement
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class AchievementState(
    val isLoading: Boolean = false,
    val achievements: List<Achievement> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)