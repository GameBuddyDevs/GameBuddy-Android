package com.example.gamebuddy.presentation.main.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ChatViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState: MutableLiveData<ChatState> = MutableLiveData(ChatState())
    val uiState: MutableLiveData<ChatState> get() = _uiState

    fun onTriggerEvent(event: ChatEvent) {
        when (event) {
            is ChatEvent.GetMessages -> getMessages()
            is ChatEvent.SendMessage -> sendMessage(event.receiverId, event.message)
        }
    }

    private fun sendMessage(receiverId: String, message: String) {
        TODO("Not yet implemented")
    }

    private fun getMessages() {
        TODO("Not yet implemented")
    }
}
