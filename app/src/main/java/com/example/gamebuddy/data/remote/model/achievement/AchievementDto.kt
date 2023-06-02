package com.example.gamebuddy.data.remote.model.achievement

import com.example.gamebuddy.domain.model.achievement.Achievement

data class AchievementDto(
    val id: String,
    val achievementName: String,
    val value: Int,
    val description: String,
    val isCollected: Boolean,
    val isEarned: Boolean
) {
    fun toAchievement(): Achievement {
        return Achievement(
            id = id,
            achievementName = achievementName,
            value = value,
            description = description,
            isCollected = isCollected,
            isEarned = isEarned,
            )
    }
}