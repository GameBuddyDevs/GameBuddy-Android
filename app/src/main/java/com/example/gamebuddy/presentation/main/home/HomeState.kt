package com.example.gamebuddy.presentation.main.home

import com.example.gamebuddy.domain.model.Pending.PendingFriends
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class HomeState(
    val isLoading: Boolean = false,
    val userId: String = "",
    val accept :Boolean = false,
    var pendingFriends: List<PendingFriends> = listOf(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
){
    fun resetPendingFriends(userId: String){
        pendingFriends = pendingFriends.filterNot { it.userId == userId }
    }
}