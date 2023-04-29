package com.example.gamebuddy.data.remote.model.forgotPassword

import com.example.gamebuddy.data.remote.model.Status

data class ForgotPasswordResponse(
    val body: ForgotPasswordBody,
    val status: Status
)