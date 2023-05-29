package com.example.gamebuddy.domain.model.achievement

data class Achievement(
    val id: String,
    val achievementName: String,
    val value: Int,
    val description: String,
    val isCollected: Boolean,
    val isEarned: Boolean
)