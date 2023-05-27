package com.example.gamebuddy.presentation.main.joincommunity

sealed class JoinCommunityEvent {

    object GetCommunities: JoinCommunityEvent()

    data class JoinCommunity(val communityId: String): JoinCommunityEvent()

    object OnRemoveHeadFromQueue: JoinCommunityEvent()
}