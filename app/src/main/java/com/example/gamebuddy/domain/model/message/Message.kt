package com.example.gamebuddy.domain.model.message

data class Message(
    val username: String = "",
    val lastMessage: String = "",
    val isRead: Boolean = false,
    val time: String = "",
    val avatar: String = ""
)