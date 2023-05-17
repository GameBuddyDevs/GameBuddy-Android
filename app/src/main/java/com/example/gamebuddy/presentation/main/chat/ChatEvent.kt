package com.example.gamebuddy.presentation.main.chat

sealed class ChatEvent {

    data class SendMessage(
        val matchedUserId: String,
        val message: String
    ) : ChatEvent()

    data class GetMessagesFromApi(
        val receiverId: String
    ) : ChatEvent()

    data class OnMessageReceivedFromWebSocket(
        val matchedUserId: String,
        val message: String
    ) : ChatEvent()

    data class AddFriend(
        val matchedUserId: String?
    ) : ChatEvent()

    data class SetUserProperties(
        val matchedUserId: String
    ) : ChatEvent()

    object OnRemoveHeadFromQueue : ChatEvent()

}
