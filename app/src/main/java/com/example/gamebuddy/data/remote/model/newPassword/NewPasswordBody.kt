package com.example.gamebuddy.data.remote.model.newPassword

import com.google.gson.annotations.SerializedName

data class NewPasswordBody (
    @SerializedName("data")
    val newPasswordData: NewPasswordData,
)