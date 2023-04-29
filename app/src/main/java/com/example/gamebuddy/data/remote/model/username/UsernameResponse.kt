package com.example.gamebuddy.data.remote.model.username

import com.example.gamebuddy.data.remote.model.Status

data class UsernameResponse (
    val body: UsernameBody,
    val status: Status
    )