package com.example.gamebuddy.data.remote.model.auth


import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("body")
    val authBody: AuthBody,
    @SerializedName("status")
    val status: AuthStatus
)