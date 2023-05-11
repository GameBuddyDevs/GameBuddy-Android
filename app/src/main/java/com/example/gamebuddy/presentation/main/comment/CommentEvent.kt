package com.example.gamebuddy.presentation.main.comment

sealed class CommentEvent {

    data class GetComments(val postId: String): CommentEvent()

    data class LikeComment(val commentId: String): CommentEvent()

    object LikeCurrentPost: CommentEvent()

    object OnRemoveHeadFromQueue : CommentEvent()
}
