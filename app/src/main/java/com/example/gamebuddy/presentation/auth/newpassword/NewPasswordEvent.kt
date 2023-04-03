package com.example.gamebuddy.presentation.auth.newpassword

sealed class NewPasswordEvent {
    data class NewPassword(
        val password: String,
        val confirmPassword: String,
    ): NewPasswordEvent()

    data class OnUpdatePassword(
        val password: String,
    ) : NewPasswordEvent()

    data class OnUpdateConfirmPassword(
        val confirmPassword: String,
    ) : NewPasswordEvent()

    object OnRemoveHeadFromQueue : NewPasswordEvent()
}