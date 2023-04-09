package com.example.gamebuddy.presentation.main.chat

import com.example.gamebuddy.domain.model.message.Message
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class MessageState(
    val query: String = "",
    val isLoading: Boolean = false,
    val messages: List<Message> = listOf(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
