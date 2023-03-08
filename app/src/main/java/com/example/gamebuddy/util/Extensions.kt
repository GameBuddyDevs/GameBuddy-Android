package com.example.gamebuddy.util

import retrofit2.HttpException


// Retrofit Extensions
fun <T> handleUseCaseException(exception: Throwable): DataState<T> {
    exception.printStackTrace()
    when (exception) {
        is HttpException -> {
            return DataState.error(
                response = Response(
                    message = exception.message(),
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            )
        }
        else -> {
            return DataState.error(
                response = Response(
                    message = exception.message ?: "Unknown error",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            )
        }
    }
}


// Queue Extensions
fun StateMessage.isMessageExistInQueue(
    queue: Queue<StateMessage>,
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