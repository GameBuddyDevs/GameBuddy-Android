package com.example.gamebuddy.data.remote.model.username

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class UsernameResponse (
    val body: UsernameBody,
    val status: AuthStatus
    )