package com.popalay.cardme.pushnotification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class NotificationActionBroadcastReceiver : BroadcastReceiver() {

    companion object {

        private const val ACTION_ALLOW = "ACTION_ALLOW"

        private const val EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID"
        private const val EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID"

        fun createAllowActionIntent(context: Context, notificationId: Int, requestId: String): PendingIntent =
            Intent(context, NotificationActionBroadcastReceiver::class.java).apply {
                action = ACTION_ALLOW
                putExtra(EXTRA_NOTIFICATION_ID, notificationId)
                putExtra(EXTRA_REQUEST_ID, requestId)
            }.let { PendingIntent.getBroadcast(context, notificationId, it, 0) }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1)
        val requestId = intent.getStringExtra(EXTRA_REQUEST_ID)

        context.getSystemService(NotificationManager::class.java).cancel(notificationId)
        when (intent.action) {
            ACTION_ALLOW -> {
                FirebaseFirestore.getInstance().collection("requests").document(requestId)
                    .update("allow", true)
                    .addOnFailureListener { Log.e("ActionReceiver", "onReceive: ", it) }
                    .addOnSuccessListener {
                        Toast.makeText(context, "Card was shared", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}