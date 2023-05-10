package com.example.gamebuddy.data.remote.model.popularGames

import com.example.gamebuddy.data.remote.model.Status
import com.example.gamebuddy.domain.model.popular.PopularGames


data class PopularGamesResponse(
    val body: PopularGamesBody,
    val status: Status
) {
    fun toPopularGames():List<PopularGames>{
        return body.data.games.map { it.toPopularGames() }
    }
}