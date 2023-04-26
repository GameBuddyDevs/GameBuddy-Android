package com.example.gamebuddy.presentation.main.match

import com.example.gamebuddy.domain.model.user.User
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class UsersState (
    val isLoading: Boolean = false,
    val users: List<User>? = null,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)