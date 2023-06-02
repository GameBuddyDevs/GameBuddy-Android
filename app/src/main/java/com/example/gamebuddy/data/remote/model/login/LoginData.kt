package com.example.gamebuddy.data.remote.model.login

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("userId")
    val pk: String,
)
