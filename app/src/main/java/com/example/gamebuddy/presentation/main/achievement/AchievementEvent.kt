package com.example.gamebuddy.presentation.main.achievement

sealed class AchievementEvent {
    object OnRemoveHeadFromQueue : AchievementEvent()
    object GetAchievement : AchievementEvent()

}