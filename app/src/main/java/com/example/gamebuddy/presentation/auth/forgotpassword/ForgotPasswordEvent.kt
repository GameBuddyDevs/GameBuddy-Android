package com.example.gamebuddy.presentation.auth.forgotpassword

sealed class ForgotPasswordEvent {
    data class ForgotPassword(
        val email: String,
        val isRegister: Boolean,
    ):ForgotPasswordEvent()

    data class OnUpdateEmail(
        val email: String,
    ) : ForgotPasswordEvent()

    object OnRemoveHeadFromQueue : ForgotPasswordEvent()
}