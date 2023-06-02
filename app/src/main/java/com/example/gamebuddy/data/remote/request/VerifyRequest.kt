package com.example.gamebuddy.data.remote.request

data class VerifyRequest(
    val email: String,
    val verificationCode: String,
)