package com.example.gamebuddy.data.remote.model.friends

import com.example.gamebuddy.data.remote.model.Status
import com.example.gamebuddy.domain.model.Friend

data class FriendsResponse(
    val body: FriendsBody,
    val status: Status
) {
    fun toFriends(): List<Friend> {
        return body.data.friends.map { it.toFriend() }
    }
}