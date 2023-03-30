package com.example.gamebuddy.presentation.auth.forgotpassword

sealed class ForgotPasswordEvent {
    data class ForgotPassword(
        val email: String,
    ):ForgotPasswordEvent()

    data class OnUpdateEmail(
        val email: String,
    ) : ForgotPasswordEvent()

    object OnRemoveHeadFromQueue : ForgotPasswordEvent()
}