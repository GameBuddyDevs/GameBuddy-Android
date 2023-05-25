package com.example.gamebuddy.presentation.main.communitydetail

import com.example.gamebuddy.presentation.main.community.CommunityEvent

sealed class CommunityDetailEvent {

    data class GetPosts(val communityId: String): CommunityDetailEvent()

    object OnRemoveHeadFromQueue : CommunityDetailEvent()
}
