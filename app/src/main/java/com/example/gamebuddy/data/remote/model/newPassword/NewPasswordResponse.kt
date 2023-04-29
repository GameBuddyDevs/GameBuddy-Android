package com.example.gamebuddy.data.remote.model.newPassword

import com.example.gamebuddy.data.remote.model.Status
import com.google.gson.annotations.SerializedName

class NewPasswordResponse(
    @SerializedName("body")
    val newPasswordBody: NewPasswordBody,
    @SerializedName("status")
    val status: Status
)