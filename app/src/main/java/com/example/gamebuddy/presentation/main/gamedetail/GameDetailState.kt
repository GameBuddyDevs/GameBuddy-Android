package com.example.gamebuddy.presentation.main.gamedetail

import com.example.gamebuddy.data.remote.model.gamedetail.GameData
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class GameDetailState(
    val isLoading: Boolean = false,
    val game: GameData? = null,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
