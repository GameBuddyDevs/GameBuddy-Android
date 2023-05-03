package com.example.gamebuddy.data.remote.model.basic

import com.example.gamebuddy.data.remote.model.Status

data class BasicResponse(
    val body: BasicBody,
    val status: Status
)