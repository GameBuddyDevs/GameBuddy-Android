package com.example.gamebuddy.presentation.main.chat

import com.example.gamebuddy.util.StateMessage

sealed class ChatBoxEvent {

    object NewQuery : ChatBoxEvent() // Query will be passed by state

    data class UpdateQuery(val query: String) : ChatBoxEvent()

    data class Error(val stateMessage: StateMessage) : ChatBoxEvent()

    object OnRemoveHeadFromQueue : ChatBoxEvent()
}