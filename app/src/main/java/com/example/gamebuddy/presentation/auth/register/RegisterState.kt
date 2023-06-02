package com.example.gamebuddy.presentation.auth.register

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isRegistrationCompleted: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
