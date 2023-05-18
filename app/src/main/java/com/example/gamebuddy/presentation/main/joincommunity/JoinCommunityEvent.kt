package com.example.gamebuddy.presentation.main.joincommunity

sealed class JoinCommunityEvent {

    object GetCommunities: JoinCommunityEvent()

    object OnRemoveHeadFromQueue: JoinCommunityEvent()
}