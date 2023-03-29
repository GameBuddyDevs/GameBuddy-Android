package com.example.gamebuddy.data.remote.model.register


import com.google.gson.annotations.SerializedName

data class AuthStatus(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)