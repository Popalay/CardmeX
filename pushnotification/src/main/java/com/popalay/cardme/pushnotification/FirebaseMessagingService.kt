package com.popalay.cardme.pushnotification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FirebaseMessaging", "onMessageReceived: $remoteMessage")
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        //TODO: Send new token
    }
}