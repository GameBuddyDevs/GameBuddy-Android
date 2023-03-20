package com.example.gamebuddy.presentation.auth.login

sealed class LoginEvent {
    data class Login(
        val email: String,
        val password: String
    ): LoginEvent()

    data class OnUpdateEmail(
        val email: String
    ): LoginEvent()

    data class OnUpdatePassword(
        val password: String
    ): LoginEvent()

    object OnRemoveHeadFromQueue: LoginEvent()
}