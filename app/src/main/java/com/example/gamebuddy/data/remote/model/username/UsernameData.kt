package com.example.gamebuddy.data.remote.model.username

import com.google.gson.annotations.SerializedName

data class UsernameData (
    @SerializedName("message")
    val message: String,
)