package com.example.gamebuddy.data.remote.model.forgotPassword

import com.google.gson.annotations.SerializedName


data class ForgotPasswordBody(
    @SerializedName("data")
    val forgotPasswordData: ForgotPasswordData,
)