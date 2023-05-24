package com.example.gamebuddy.data.remote.model.achievement
import com.google.gson.annotations.SerializedName

data class AchievementData(
    @SerializedName("achievementList")
    val achievementDto: List<AchievementDto>
)