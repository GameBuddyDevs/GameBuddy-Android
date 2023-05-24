package com.example.gamebuddy.data.remote.model.achievement

import com.example.gamebuddy.data.remote.model.Status
import com.example.gamebuddy.domain.model.achievement.Achievement

data class AchievementResponse(
    val body: Body,
    val status: Status
) {
    fun toAchievement(): List<Achievement> {
        return body.data.achievementDto.map { it.toAchievement() }
    }
}