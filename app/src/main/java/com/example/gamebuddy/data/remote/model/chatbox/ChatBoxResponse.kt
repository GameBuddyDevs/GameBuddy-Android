package com.example.gamebuddy.data.remote.model.chatbox

import com.example.gamebuddy.data.remote.model.Status

data class ChatBoxResponse(
    val body: Body,
    val status: Status
)