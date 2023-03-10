package com.example.gamebuddy.data.remote.model


import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("token")
    val token: String
)