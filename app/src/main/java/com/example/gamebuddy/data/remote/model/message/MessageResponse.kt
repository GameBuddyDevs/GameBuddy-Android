package com.example.gamebuddy.data.remote.model.message

import com.example.gamebuddy.data.remote.model.Status

data class MessageResponse(
    val body: Body,
    val status: Status
)