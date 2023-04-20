package com.example.gamebuddy.data.remote.model.validate

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class ValidateResponse(
    val body: ValidateBody,
    val status: AuthStatus
)