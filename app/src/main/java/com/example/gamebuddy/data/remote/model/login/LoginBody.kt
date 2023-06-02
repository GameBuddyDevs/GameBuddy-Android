package com.example.gamebuddy.data.remote.model.login

import com.google.gson.annotations.SerializedName

data class LoginBody(
    @SerializedName("data")
    val loginData: LoginData
)
