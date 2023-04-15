package com.example.gamebuddy.data.remote.model.games

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class GamesResponse (
    val body: GamesBody,
    val status: AuthStatus
)