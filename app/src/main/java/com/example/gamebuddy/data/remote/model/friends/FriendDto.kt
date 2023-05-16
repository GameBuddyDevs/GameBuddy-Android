package com.example.gamebuddy.data.remote.model.friends

import com.example.gamebuddy.domain.model.Friend
import com.example.gamebuddy.domain.model.profile.AllFriends

data class FriendDto(
    val userId:String,
    val username: String,
    val age: Int,
    val country: String,
    val avatar: String,
) {
    fun toFriend(): AllFriends {
        return AllFriends(
            userId = userId,
            username = username,
            country = country,
            avatar = avatar
        )
    }
}