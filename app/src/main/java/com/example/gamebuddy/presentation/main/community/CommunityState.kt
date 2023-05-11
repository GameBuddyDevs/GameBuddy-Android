package com.example.gamebuddy.presentation.main.community

import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class CommunityState(
    val isLoading: Boolean = false,
    val posts: List<Post> = mutableListOf(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
