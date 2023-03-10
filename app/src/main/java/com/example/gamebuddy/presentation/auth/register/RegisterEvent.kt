package com.example.gamebuddy.presentation.auth.register

sealed class RegisterEvent {

    data class Register(
        val email: String,
        val password: String,
        val confirmPassword: String,
    ) : RegisterEvent()

    data class OnUpdateEmail(
        val email: String,
    ) : RegisterEvent()

    data class OnUpdatePassword(
        val password: String,
    ) : RegisterEvent()

    data class OnUpdateConfirmPassword(
        val confirmPassword: String,
    ) : RegisterEvent()

    object OnRemoveHeadFromQueue : RegisterEvent()
}