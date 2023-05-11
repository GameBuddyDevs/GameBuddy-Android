package com.example.gamebuddy.data.remote.request

data class CreateCommentRequest(
    val postId: String,
    val message: String,
)
