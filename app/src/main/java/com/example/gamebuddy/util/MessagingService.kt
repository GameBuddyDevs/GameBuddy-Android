package com.example.gamebuddy.util

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MessagingService: FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.let {
            it.notification?.let {
                val title = it.title
                val body = it.body
                Timber.d("onMessageReceived: title: $title")
                Timber.d("onMessageReceived: body: $body")
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        token.let {
            Timber.d("onNewToken: $it")
        }
    }
}