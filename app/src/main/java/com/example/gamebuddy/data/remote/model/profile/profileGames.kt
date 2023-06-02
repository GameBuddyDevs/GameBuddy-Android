package com.example.gamebuddy.data.remote.model.profile

data class profileGames(
    val gameId:String,
    val gameName:String,
    val gameIcon:String,
    val category:String,
    val avgVote:Double,
    val description:String
)