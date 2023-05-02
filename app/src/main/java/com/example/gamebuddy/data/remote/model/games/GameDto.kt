package com.example.gamebuddy.data.remote.model.games

import com.example.gamebuddy.domain.model.game.Game

data class GameDto(
    val avgVote: Double,
    val category: Any,
    val description: String,
    val gameIcon: String,
    val gameId: String,
    val gameName: String

) {
    fun toGame(): Game {
        return Game(
            id = gameId,
            name = gameName,
            icon = gameIcon,
            description = description,
        )
    }
}