package com.example.gamebuddy.presentation.main.community


sealed class CommunityEvent {

    data class GetPosts(val communityId: String): CommunityEvent()

    data class LikePost(val postId: String): CommunityEvent()

    object OnRemoveHeadFromQueue : CommunityEvent()
}
