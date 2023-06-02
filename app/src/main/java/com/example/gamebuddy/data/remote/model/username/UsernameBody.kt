package com.example.gamebuddy.data.remote.model.username
import com.google.gson.annotations.SerializedName
data class UsernameBody (
    @SerializedName("data")
    val usernameData: UsernameData,
)