package com.example.gamebuddy.data.remote.model.games

import com.google.gson.annotations.SerializedName

data class GameData(
    @SerializedName("games")
    val gameDto: List<GameDto>
)