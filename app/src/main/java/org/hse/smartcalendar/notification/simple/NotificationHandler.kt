package org.hse.smartcalendar.notification.simple

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import org.hse.smartcalendar.R
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    // SIMPLE NOTIFICATION
    fun showSimpleNotification() {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle("Simple Notification")
            .setContentText("Message or text with notification")
            .setSmallIcon(R.drawable.round_notifications_24)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .setWhen(System.currentTimeMillis())//current time in milliseconds
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}