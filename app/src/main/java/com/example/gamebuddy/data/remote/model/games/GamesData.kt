package com.example.gamebuddy.data.remote.model.games

import com.google.gson.annotations.SerializedName

data class GamesData (
    @SerializedName("gameId")
    val gameId:String,
    @SerializedName("gameName")
    val gameName:String,
    @SerializedName("gameIcon")
    val gameIcon:List<String>,
    @SerializedName("category")
    val category:String,
    @SerializedName("avgVote")
    val avgVote:Int,
    @SerializedName("description")
    val description:String,
)