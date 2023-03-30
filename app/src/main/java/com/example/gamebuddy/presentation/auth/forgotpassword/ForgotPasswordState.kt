package com.example.gamebuddy.presentation.auth.forgotpassword

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class ForgotPasswordState(
    val isLoading: Boolean = false,
    val email: String = "",
    val isForgotPasswordCompleted: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)