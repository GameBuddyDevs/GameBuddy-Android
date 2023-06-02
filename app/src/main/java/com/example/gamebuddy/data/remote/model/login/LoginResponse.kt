package com.example.gamebuddy.data.remote.model.login

import com.example.gamebuddy.data.remote.model.Status

data class LoginResponse(
    val body: LoginBody,
    val status: Status
)