package com.example.gamebuddy.data.remote.model.gamedetail

import com.example.gamebuddy.data.remote.model.Status

data class GameDetailResponse(
    val body: Body,
    val status: Status
)