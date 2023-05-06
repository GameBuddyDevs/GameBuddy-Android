package com.example.gamebuddy.data.remote.model.post

import com.example.gamebuddy.data.remote.model.Status

data class PostResponse(
    val body: Body,
    val status: Status
)