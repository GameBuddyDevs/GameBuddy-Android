package com.example.gamebuddy.data.remote.model.register


import com.google.gson.annotations.SerializedName

data class RegisterData(
    @SerializedName("userId")
    val userId: String,
)