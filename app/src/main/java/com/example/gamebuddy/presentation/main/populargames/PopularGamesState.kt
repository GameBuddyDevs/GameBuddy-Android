package com.example.gamebuddy.presentation.main.populargames

import com.example.gamebuddy.domain.model.popular.PopularGames
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class PopularGamesState(
    val isLoading: Boolean = false,
    val games: List<PopularGames> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)