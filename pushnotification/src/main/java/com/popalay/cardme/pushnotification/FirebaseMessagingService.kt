package com.popalay.cardme.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.popalay.cardme.api.data.repository.TokenRepository
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.inject

class FirebaseMessagingService : FirebaseMessagingService() {

    private val tokenRepository: TokenRepository by inject()

    companion object {

        private const val ADD_CARD_REQUEST_CHANNEL_ID = "ADD_CARD_REQUEST_CHANNEL_ID"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        FirebaseAuth.getInstance().currentUser?.uid ?: return

        val notificationManager = getSystemService<NotificationManager>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Add card requests"
            val description = "When a user wants to add your card, you get a notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ADD_CARD_REQUEST_CHANNEL_ID, name, importance)
            channel.description = description
            notificationManager?.createNotificationChannel(channel)
        }

        val notificationId = requireNotNull(remoteMessage.data["notificationId"]).hashCode()
        val requestId = requireNotNull(remoteMessage.data["requestId"])
        val icon = requireNotNull(remoteMessage.data["icon"])
        val title = requireNotNull(remoteMessage.data["title"])
        val description = requireNotNull(remoteMessage.data["description"])

        val allowPendingIntent: PendingIntent = NotificationActionBroadcastReceiver.createAllowActionIntent(
            applicationContext,
            notificationId = notificationId,
            requestId = requestId
        )

        val notification = NotificationCompat.Builder(applicationContext, ADD_CARD_REQUEST_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(Picasso.get().load(icon).transform(CircleImageTransformation()).get())
            .setContentTitle(title)
            .setContentText(description)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.ic_allow, "Allow", allowPendingIntent)
            .build()

        notificationManager?.notify(notificationId, notification)
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        tokenRepository.syncToken(token ?: "").blockingAwait()
    }
}