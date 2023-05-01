package com.example.gamebuddy.websocket

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import timber.log.Timber

class WebSocketClient(
    private val onConnectionOpened: () -> Unit,
    private val onConnectionClosed: () -> Unit,
    private val onMessageReceived: (String) -> Unit
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        onConnectionOpened()
        webSocket.send("Android Device Connected")
        Timber.d("Connection Opened. Response: $response)")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        onMessageReceived(text)
        Timber.d("Message Received: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Timber.d("Connection closing. Reason: $reason)")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        onConnectionClosed()
        Timber.d("Connection closed. Reason: $reason)")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Timber.d("Connection failed. Reason: $t)")
    }
}