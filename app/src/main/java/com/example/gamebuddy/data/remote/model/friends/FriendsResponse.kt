package com.example.gamebuddy.data.remote.model.friends

import com.example.gamebuddy.data.remote.model.Status

data class FriendsResponse(
    val body: FriendsBody,
    val status: Status
)