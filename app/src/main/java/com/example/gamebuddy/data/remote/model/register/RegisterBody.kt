package com.example.gamebuddy.data.remote.model.register


import com.google.gson.annotations.SerializedName

data class RegisterBody(
    @SerializedName("data")
    val registerData: RegisterData
)