package com.example.gamebuddy.data.remote.model.comment

import com.example.gamebuddy.data.remote.model.Status

data class CommentResponse(
    val body: Body,
    val status: Status
)