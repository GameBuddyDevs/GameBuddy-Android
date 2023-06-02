package com.example.gamebuddy.presentation.main.communitydetail

import com.example.gamebuddy.data.remote.model.post.Post
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class CommunityDetailState(
    val isLoading: Boolean = false,
    val isJoined: Boolean = false,
    val posts: List<Post> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
