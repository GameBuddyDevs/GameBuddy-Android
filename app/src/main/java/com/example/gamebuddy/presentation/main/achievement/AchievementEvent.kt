package com.example.gamebuddy.presentation.main.achievement

sealed class AchievementEvent {

    object GetAchievement : AchievementEvent()

    data class CollectAchievement(val achievementId: String) : AchievementEvent()

    object OnRemoveHeadFromQueue : AchievementEvent()
}