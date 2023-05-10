package com.example.gamebuddy.presentation.main.comment

import com.example.gamebuddy.data.remote.model.comment.Comment
import com.example.gamebuddy.util.Queue
import com.example.gamebuddy.util.StateMessage

data class CommentState(
    val isLoading: Boolean = false,
    val comments: List<Comment> = listOf(),
    val queue: Queue<StateMessage> = Queue(mutableListOf()),
)
