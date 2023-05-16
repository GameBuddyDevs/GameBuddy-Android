package com.example.gamebuddy.presentation.main.profile

import com.example.gamebuddy.domain.model.profile.AllFriends
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class AllFriendsState(
    val isLoading: Boolean = false,
    val allFriends: List<AllFriends>? = null,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)