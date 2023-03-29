package com.example.gamebuddy.data.remote.model.verify

import com.example.gamebuddy.data.remote.model.register.AuthStatus
import com.google.gson.annotations.SerializedName

data class VerifyResponse(
    @SerializedName("body")
    val verifyBody: VerifyBody,
    @SerializedName("status")
    val status: AuthStatus
)

