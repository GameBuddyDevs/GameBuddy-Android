package com.example.gamebuddy.data.remote.model


import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)