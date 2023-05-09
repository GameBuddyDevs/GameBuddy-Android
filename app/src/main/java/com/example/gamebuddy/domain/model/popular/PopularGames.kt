package com.example.gamebuddy.domain.model.popular

data class PopularGames(
    val gameId:String,
    val gameName:String,
    val gameIcon:String,
    val category:String,
    val avgVote:Double
)