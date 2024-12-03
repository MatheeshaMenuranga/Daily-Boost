package com.example.dailyboost

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Notification
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("reminder_title") ?: "Reminder"
        val description = intent.getStringExtra("reminder_description") ?: "No description"
        val hour = intent.getIntExtra("reminder_hour", -1)
        val minute = intent.getIntExtra("reminder_minute", -1)

        // Show notification
        showNotification(context, title, description)

        // Start an activity to display the reminder
        val displayIntent = Intent(context, DisplayReminderActivity::class.java).apply {
            putExtra("reminder_title", title)
            putExtra("reminder_description", description)
            putExtra("reminder_hour", hour)
            putExtra("reminder_minute", minute)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Needed to start a new task
        }
        context.startActivity(displayIntent)
    }

    private fun showNotification(context: Context, title: String, description: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "reminder_channel",
                "Reminder Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_add) // Ensure this drawable exists
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }
}
