package com.example.gamebuddy.presentation.main.community

import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class CommunityState(
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
