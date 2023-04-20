package com.example.gamebuddy.presentation.main.chat

import com.example.gamebuddy.util.StateMessage

sealed class MessageEvent {

    object NewQuery : MessageEvent() // Query will be passed by state

    data class UpdateQuery(val query: String) : MessageEvent()

    data class Error(val stateMessage: StateMessage) : MessageEvent()

    object OnRemoveHeadFromQueue : MessageEvent()
}