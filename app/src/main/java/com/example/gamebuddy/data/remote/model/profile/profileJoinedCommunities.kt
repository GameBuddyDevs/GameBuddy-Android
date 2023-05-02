package com.example.gamebuddy.data.remote.model.profile

data class profileJoinedCommunities(
    val communityId:String,
    val name:String,
    val communityAvatar:String,
    val isOwner:Boolean
)