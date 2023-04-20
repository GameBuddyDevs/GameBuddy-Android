package com.example.gamebuddy.data.remote.model.setprofile

import com.example.gamebuddy.data.remote.model.register.AuthStatus

data class SetDetailsResponse(
    val body: SetDetailsBody,
    val status: AuthStatus
)