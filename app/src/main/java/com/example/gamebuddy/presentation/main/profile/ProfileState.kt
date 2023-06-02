package com.example.gamebuddy.presentation.main.profile

import com.example.gamebuddy.domain.model.profile.profilUser
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class ProfileState(
    val isLoading: Boolean = false,
    val profileUser: profilUser? = null,
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)