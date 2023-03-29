package com.example.gamebuddy.util


data class StateMessage(val response: Response)

data class Response(
    val message: String?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

sealed class UIComponentType {

    class Dialog : UIComponentType()

    class Toast : UIComponentType()

    class AreYouSureDialog(
        val callback: AreYouSureCallback
    ) : UIComponentType()

    class None : UIComponentType()
}

sealed class MessageType {

    class Success : MessageType()

    class Error : MessageType()

    class Info : MessageType()

    class None : MessageType()
}

fun StateMessage.doesMessageAlreadyExistInQueue(
    queue: Queue<StateMessage>,
): Boolean {
    for (item in queue.items) {
        if (item.response.message == response.message) {
            return true
        }
        if (item.response.messageType == response.messageType) {
            return true
        }
        if (item.response.uiComponentType == response.uiComponentType) {
            return true
        }
    }
    return false
}

interface StateMessageCallback {

    fun removeMessageFromStack()
}

interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}
