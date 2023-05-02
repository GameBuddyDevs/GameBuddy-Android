package com.example.gamebuddy.presentation.main.chat

sealed class ChatEvent {

    data class SendMessage(
        val receiverId: String,
        val message: String
    ) : ChatEvent()

    data class GetMessages(
        val receiverId: String
    ) : ChatEvent()

    data class AddFriend(
        val matchedUserId: String?
    ) : ChatEvent()

    data class SetUserProperties(
        val matchedUserId: String
    ) : ChatEvent()

    object OnRemoveHeadFromQueue : ChatEvent()

}
