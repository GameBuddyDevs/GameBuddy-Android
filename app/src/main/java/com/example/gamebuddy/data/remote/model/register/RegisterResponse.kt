package com.example.gamebuddy.data.remote.model.register


import com.example.gamebuddy.data.remote.model.Status
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("body")
    val registerBody: RegisterBody,
    @SerializedName("status")
    val status: Status
)