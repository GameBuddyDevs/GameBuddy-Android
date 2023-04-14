package com.example.gamebuddy.presentation.auth.details.detailsKeyword

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class KeywordState(
    val isLoading: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
