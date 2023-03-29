package com.example.gamebuddy.data.remote.model.register


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("body")
    val registerBody: RegisterBody,
    @SerializedName("status")
    val status: AuthStatus
)