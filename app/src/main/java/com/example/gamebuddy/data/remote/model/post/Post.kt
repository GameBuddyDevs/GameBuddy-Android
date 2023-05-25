package com.example.gamebuddy.data.remote.model.post


data class Post(
    val avatar: String,
    val body: String,
    val commentCount: Int,
    val likeCount: Int,
    val picture: String,
    val postId: String,
    val title: String,
    val updatedDate: String,
    val username: String,
    val communityName: String
)