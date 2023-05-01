package com.example.gamebuddy.presentation.main.chat

data class ChatState(
    val socketStatus: Boolean = false,
    val isMatchedUserConnected: Boolean = false,
    val isLoading: Boolean = false,
    val messages: List<String> = emptyList(),
)
