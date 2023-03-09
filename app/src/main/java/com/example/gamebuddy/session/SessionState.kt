package com.example.gamebuddy.session

import com.example.gamebuddy.domain.model.account.AuthToken
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class SessionState(
    val isLoading: Boolean = false,
    val authToken: AuthToken? = null,
    val didCheckForPreviousAuthUser: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
