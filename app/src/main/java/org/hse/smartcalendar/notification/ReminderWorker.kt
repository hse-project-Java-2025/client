package org.hse.smartcalendar.notification

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.hse.smartcalendar.R
import org.hse.smartcalendar.activity.BaseApplication
import org.hse.smartcalendar.activity.MainActivity

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    // Arbitrary id number
    private val notificationId = 17

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE//maybe change
        )

        val type = inputData.getString(TYPE_KEY)
        val title = inputData.getString(TITLE_KEY)
        val startTime = inputData.getString(START_KEY)
        val endTime = inputData.getString(END_KEY)
        val desc = inputData.getString(MESSAGE_KEY)
        val delayMinutes = inputData.getInt(BEFORE_KEY, -1)

        val body = "The task \"$title\"($startTime - $endTime) type $type starts in $delayMinutes minutes"
        val builder = NotificationCompat.Builder(applicationContext, BaseApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.timer)
            .setContentTitle("Smart Calendar")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }

        return Result.success()
    }

    companion object {
        const val TYPE_KEY = "TYPE"
        const val TITLE_KEY = "TITLE"
        const val START_KEY = "START"
        const val END_KEY = "END"
        const val MESSAGE_KEY = "MESSAGE"
        const val BEFORE_KEY = "BEFORE"
    }
}