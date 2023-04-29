package com.example.gamebuddy.data.remote.model.friends

import com.example.gamebuddy.domain.model.Friend

data class FriendDto(
    val age: Int,
    val avatar: String,
    val country: String,
    val lastOnlineDate: String,
    val username: String
) {
    fun toFriend(): Friend {
        return Friend(
            age = age,
            avatar = avatar,
            username = username
        )
    }
}