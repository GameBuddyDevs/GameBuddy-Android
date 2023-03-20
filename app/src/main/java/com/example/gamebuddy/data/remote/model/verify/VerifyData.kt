package com.example.gamebuddy.data.remote.model.verify

import com.google.gson.annotations.SerializedName

data class VerifyData(
    @SerializedName("accessToken")
    val accessToken: String,
)
