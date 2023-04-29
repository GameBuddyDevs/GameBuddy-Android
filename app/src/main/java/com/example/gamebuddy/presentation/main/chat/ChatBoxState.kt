package com.example.gamebuddy.presentation.main.chat

import com.example.gamebuddy.domain.model.Friend
import com.example.gamebuddy.domain.model.message.Message
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class ChatBoxState(
    val query: String = "",
    val isLoading: Boolean = false,
    val friends: List<Friend> = listOf(),
    val chatBox: List<Message> = listOf(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
