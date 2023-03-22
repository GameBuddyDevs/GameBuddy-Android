package com.example.gamebuddy.presentation.auth.verify

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class VerifyState(
    val isLoading: Boolean = false,
    val verificationCode: String = "",
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
