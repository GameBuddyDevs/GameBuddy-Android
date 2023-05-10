package com.example.gamebuddy.presentation.main.comment

sealed class CommentEvent {

    data class GetComments(val postId: String): CommentEvent()

    object OnRemoveHeadFromQueue : CommentEvent()
}
