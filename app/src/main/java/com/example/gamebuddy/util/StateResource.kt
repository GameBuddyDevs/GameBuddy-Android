package com.example.gamebuddy.util


data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

sealed class UIComponentType {

    class Dialog : UIComponentType()

    class Toast: UIComponentType()

    class AreYouSureDialog(
        val callback: AreYouSureCallback
    ): UIComponentType()

    class None : UIComponentType()
}

sealed class MessageType {

    class Success : MessageType()

    class Error : MessageType()

    class Info : MessageType()

    class None : MessageType()
}

interface StateMessageCallback {

    fun removeMessageFromStack()
}

interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}
