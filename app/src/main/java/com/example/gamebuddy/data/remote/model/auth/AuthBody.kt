package com.example.gamebuddy.data.remote.model.auth


import com.google.gson.annotations.SerializedName

data class AuthBody(
    @SerializedName("data")
    val authData: AuthData
)