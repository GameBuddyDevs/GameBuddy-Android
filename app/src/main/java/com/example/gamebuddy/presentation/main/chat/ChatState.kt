package com.example.gamebuddy.presentation.main.chat

import com.example.gamebuddy.data.remote.model.message.Conversation
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class ChatState(
    val socketStatus: Boolean = false,
    val isMatchedUserConnected: Boolean = false,
    val matchedUserId: String = "",
    val isFriendRequestSend: Boolean = false,
    val isLoading: Boolean = false,
    val messages: List<Conversation> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
