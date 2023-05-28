package com.example.gamebuddy.presentation.main.communitydetail


sealed class CommunityDetailEvent {

    data class GetPosts(val communityId: String): CommunityDetailEvent()

    data class JoinCommunity(val communityId: String): CommunityDetailEvent()

    data class LeaveCommunity(val communityId: String): CommunityDetailEvent()

    object OnRemoveHeadFromQueue : CommunityDetailEvent()
}
