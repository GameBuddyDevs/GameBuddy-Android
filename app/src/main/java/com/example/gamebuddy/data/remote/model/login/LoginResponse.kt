package com.example.gamebuddy.data.remote.model.login

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class LoginResponse(
    val body: LoginBody,
    val status: AuthStatus
)