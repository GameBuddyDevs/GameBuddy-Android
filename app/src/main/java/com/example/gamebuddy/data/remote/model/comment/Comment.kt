package com.example.gamebuddy.data.remote.model.comment

data class Comment(
    val avatar: String,
    val commentId: String,
    val likeCount: Int,
    val message: String,
    val updatedDate: String,
    val username: String
)