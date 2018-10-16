package com.popalay.cardme.pushnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso

class FirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        private const val ADD_CARD_REQUEST_CHANNEL_ID = "ADD_CARD_REQUEST_CHANNEL_ID"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Add card requests"
            val description = "When a user wants to add your card, you get a notification"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(ADD_CARD_REQUEST_CHANNEL_ID, name, importance)
            channel.description = description
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = requireNotNull(remoteMessage.data["notificationId"]).hashCode()
        val requestId = requireNotNull(remoteMessage.data["requestId"])

        val notification = remoteMessage.notification?.run {

            val allowPendingIntent: PendingIntent = NotificationActionBroadcastReceiver.createAllowActionIntent(
                this@FirebaseMessagingService,
                notificationId = notificationId,
                requestId = requestId
            )

            NotificationCompat.Builder(this@FirebaseMessagingService, ADD_CARD_REQUEST_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(Picasso.get().load(icon).transform(CircleImageTransformation()).get())
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_allow, "Allow", allowPendingIntent)
                .build()
        }

        notificationManager.notify(notificationId, notification)
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        FirebaseAuth.getInstance().currentUser?.uid?.also { userId ->
            val firestoreToken = Token(userId, token ?: "")
            Tasks.await(FirebaseFirestore.getInstance().collection("token").document(userId).set(firestoreToken))
        }
    }
}