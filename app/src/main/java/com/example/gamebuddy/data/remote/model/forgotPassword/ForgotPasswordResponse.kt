package com.example.gamebuddy.data.remote.model.forgotPassword

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class ForgotPasswordResponse(
    val body: ForgotPasswordBody,
    val status: AuthStatus
)