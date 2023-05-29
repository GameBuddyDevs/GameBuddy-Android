package com.example.gamebuddy.data.remote.model.achievement
import com.google.gson.annotations.SerializedName

data class AchievementData(
    @SerializedName("achievementsList")
    val achievementDto: List<AchievementDto>
)