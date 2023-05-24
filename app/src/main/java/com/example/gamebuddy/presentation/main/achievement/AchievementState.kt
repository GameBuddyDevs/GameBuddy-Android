package com.example.gamebuddy.presentation.main.achievement

import com.example.gamebuddy.domain.model.achievement.Achievement
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class AchievementState(
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
    val isLoading: Boolean = false,
    val earnedAchievements: List<Achievement>? = listOf(),
    val unearnedAchievements: List<Achievement>? = listOf(),
    val collectedAchievements: List<Achievement>? = listOf(),

    )