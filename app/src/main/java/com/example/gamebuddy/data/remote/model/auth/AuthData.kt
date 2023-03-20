package com.example.gamebuddy.data.remote.model.auth


import com.google.gson.annotations.SerializedName

data class AuthData(
    @SerializedName("userId")
    val userId: String,
)