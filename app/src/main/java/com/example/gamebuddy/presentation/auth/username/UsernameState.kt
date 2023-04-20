package com.example.gamebuddy.presentation.auth.username

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class UsernameState (
    val isLoading: Boolean = false,
    val username: String = "",
    val isUsernameCompleted: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)