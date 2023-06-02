package com.example.gamebuddy.presentation.main.chatbox

import com.example.gamebuddy.util.StateMessage

sealed class ChatBoxEvent {

    object NewQuery : ChatBoxEvent() // Query will be passed by state

    object NewSearch : ChatBoxEvent()

    data class UpdateQuery(val query: String) : ChatBoxEvent()

    object GetFriends : ChatBoxEvent()

    object GetChatBox : ChatBoxEvent()

    data class Error(val stateMessage: StateMessage) : ChatBoxEvent()

    object OnRemoveHeadFromQueue : ChatBoxEvent()
}