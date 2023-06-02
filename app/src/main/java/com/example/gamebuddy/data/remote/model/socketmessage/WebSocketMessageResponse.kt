package com.example.gamebuddy.data.remote.model.socketmessage

data class WebSocketMessageResponse(
    val id: String,
    val message: String,
    val senderId: String,
    val senderName: String
)