package com.example.gamebuddy.presentation.main.community


sealed class CommunityEvent {

    object GetPosts: CommunityEvent()

    data class LikePost(val postId: String): CommunityEvent()

    object OnRemoveHeadFromQueue : CommunityEvent()
}
