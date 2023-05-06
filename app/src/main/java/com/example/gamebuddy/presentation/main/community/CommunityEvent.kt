package com.example.gamebuddy.presentation.main.community


sealed class CommunityEvent {

    data class GetPosts(val communityId: String): CommunityEvent()

    object OnRemoveHeadFromQueue : CommunityEvent()
}
