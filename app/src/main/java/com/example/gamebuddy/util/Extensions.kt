package com.example.gamebuddy.util

fun StateMessage.isMessageExistInQueue(
    queue: Queue<StateMessage>
): Boolean {

    queue.items.forEach { stateMessage ->
        if (stateMessage.response.message == this.response.message) {
            return true
        }
        if (stateMessage.response.messageType == this.response.messageType) {
            return true
        }
        if (stateMessage.response.uiComponentType == this.response.uiComponentType) {
            return true
        }
    }

    return false
}