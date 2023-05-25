package com.example.gamebuddy.data.remote.model.joincommunity

data class Community(
    val communityAvatar: String,
    val communityId: String,
    val createdDate: String,
    val description: String,
    val memberCount: Int,
    val name: String,
    val wallpaper: String,
    val isJoined: Boolean
)