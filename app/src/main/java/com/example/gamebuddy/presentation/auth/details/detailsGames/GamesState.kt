package com.example.gamebuddy.presentation.auth.details.detailsGames

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class GamesState (
    val isLoading: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)