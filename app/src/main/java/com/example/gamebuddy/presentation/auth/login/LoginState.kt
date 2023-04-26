package com.example.gamebuddy.presentation.auth.login

import com.example.gamebuddy.util.AuthActionType
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoginCompleted: Boolean = false,
    val actionType: AuthActionType = AuthActionType.LOGIN,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)