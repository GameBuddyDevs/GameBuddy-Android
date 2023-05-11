package com.example.gamebuddy.presentation.main.market

import com.example.gamebuddy.domain.model.market.Market
import com.example.gamebuddy.util.StateMessage
import com.example.gamebuddy.util.Queue


data class MarketState(
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
    val isLoading: Boolean = false,
    val avatars: List<Market>? = listOf(),
)