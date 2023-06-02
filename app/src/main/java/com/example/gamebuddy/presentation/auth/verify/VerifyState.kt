package com.example.gamebuddy.presentation.auth.verify

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class VerifyState(
    val isLoading: Boolean = false,
    val verificationCode: String = "",
    val isVerifyCompleted: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
