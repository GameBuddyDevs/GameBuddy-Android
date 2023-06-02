package com.example.gamebuddy.data.remote.request

data class SendMessageRequest(
    val receiverId: String,
    val message: String
)
