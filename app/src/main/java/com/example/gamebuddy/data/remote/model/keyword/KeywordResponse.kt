package com.example.gamebuddy.data.remote.model.keyword

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class KeywordResponse(
    val body: Body,
    val status: AuthStatus
)