package com.example.gamebuddy.presentation.main.communitydetail


sealed class CommunityDetailEvent {

    data class GetPosts(val communityId: String): CommunityDetailEvent()

    data class FollowCommunity(val communityId: String): CommunityDetailEvent()

    object OnRemoveHeadFromQueue : CommunityDetailEvent()
}
