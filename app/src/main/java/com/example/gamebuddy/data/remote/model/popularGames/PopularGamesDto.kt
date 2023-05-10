package com.example.gamebuddy.data.remote.model.popularGames

import com.example.gamebuddy.domain.model.popular.PopularGames

data class PopularGamesDto(
    val gameId:String,
    val gameName:String,
    val gameIcon:String,
    val category:String,
    val avgVote:Double,
    val description:String
) {
    fun toPopularGames(): PopularGames{
        return PopularGames(
            gameId = gameId,
            gameName = gameName,
            gameIcon = gameIcon,
            category = category,
            avgVote = avgVote
        )
    }
}