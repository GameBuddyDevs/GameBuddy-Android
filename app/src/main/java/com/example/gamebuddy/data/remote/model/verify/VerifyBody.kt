package com.example.gamebuddy.data.remote.model.verify

import com.google.gson.annotations.SerializedName

data class VerifyBody(
    @SerializedName("data")
    val verifyData: VerifyData
)
