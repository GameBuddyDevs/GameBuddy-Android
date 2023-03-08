package com.example.gamebuddy.session

data class SessionState(
    val isLoading: Boolean = false,
    val authToken: String? = "",
    val didCheckForPreviousAuthUser: Boolean = false,
)
