package com.example.gamebuddy.presentation.auth.username

sealed class UsernameEvent {
    data class Username(
        val username:String
    ): UsernameEvent()

    data class OnUpdateUsername(
        val username: String
    ): UsernameEvent()

    object OnRemoveHeadFromQueue: UsernameEvent()
}