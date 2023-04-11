package com.example.gamebuddy.presentation.auth.details.detailsUser

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class DetailsUserState(
    val isLoading: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
    )
