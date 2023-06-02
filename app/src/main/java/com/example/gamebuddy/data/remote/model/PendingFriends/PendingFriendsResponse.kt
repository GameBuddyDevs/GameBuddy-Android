package com.example.gamebuddy.data.remote.model.PendingFriends
import com.example.gamebuddy.data.remote.model.Status
import com.example.gamebuddy.domain.model.Pending.PendingFriends

data class PendingFriendsResponse(
    val body:PendingFriendsBody,
    val status:Status
){
    fun toPendingFriends():List<PendingFriends> {
        return body.data.friends.map { it.toPendingFriend() }
    }
}