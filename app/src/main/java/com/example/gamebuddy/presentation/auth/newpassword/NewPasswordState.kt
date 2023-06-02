package com.example.gamebuddy.presentation.auth.newpassword

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class NewPasswordState (
    val isLoading: Boolean = false,
    val password: String = "",
    val confirmPassword: String = "",
    val isNewPasswordCompleted: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)