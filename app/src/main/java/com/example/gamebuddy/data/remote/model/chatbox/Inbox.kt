package com.example.gamebuddy.data.remote.model.chatbox

data class Inbox(
    val avatar: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val userId: String,
    val username: String
)