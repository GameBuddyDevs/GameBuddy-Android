package com.example.gamebuddy.data.remote.model.games

import com.example.gamebuddy.data.remote.model.register.AuthStatus
import com.example.gamebuddy.domain.model.game.Game

data class GameResponse(
    val body: Body,
    val status: AuthStatus
) {
    fun toGames(): List<Game> {
        return body.data.gameDto.map { it.toGame() }
    }
}