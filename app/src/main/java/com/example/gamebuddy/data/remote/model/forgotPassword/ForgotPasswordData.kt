package com.example.gamebuddy.data.remote.model.forgotPassword

import com.google.gson.annotations.SerializedName

data class ForgotPasswordData(
    @SerializedName("message")
    val message: String,
)