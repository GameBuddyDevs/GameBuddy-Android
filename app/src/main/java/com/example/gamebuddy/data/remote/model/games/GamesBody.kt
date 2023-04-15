package com.example.gamebuddy.data.remote.model.games

import com.google.gson.annotations.SerializedName

data class GamesBody (
    @SerializedName("data")
    val gamesData: GamesData
    )