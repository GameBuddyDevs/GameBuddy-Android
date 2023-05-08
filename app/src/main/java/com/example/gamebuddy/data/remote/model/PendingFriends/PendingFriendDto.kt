package com.example.gamebuddy.data.remote.model.PendingFriends

import com.example.gamebuddy.domain.model.Pending.PendingFriends

data class PendingFriendDto(
    val userId:String,
    val username:String,
    val age:Int,
    val country:String,
    val avatar:String
){
    fun toPendingFriend(): PendingFriends{
        return PendingFriends(
            userId = userId,
            username =  username,
            country = country,
            avatar = avatar
        )
    }
}