package com.example.gamebuddy.presentation.main.joincommunity

import com.example.gamebuddy.data.remote.model.joincommunity.Community
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class JoinCommunityState(
    val isLoading: Boolean = false,
    val communities: List<Community> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
