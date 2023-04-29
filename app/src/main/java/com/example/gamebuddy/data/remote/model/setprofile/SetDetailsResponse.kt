package com.example.gamebuddy.data.remote.model.setprofile

import com.example.gamebuddy.data.remote.model.Status

data class SetDetailsResponse(
    val body: SetDetailsBody,
    val status: Status
)