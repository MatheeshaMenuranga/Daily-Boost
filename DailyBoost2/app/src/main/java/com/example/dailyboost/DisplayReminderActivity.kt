package com.example.dailyboost

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DisplayReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_reminder)

        val reminderTitle = intent.getStringExtra("reminder_title")
        val reminderDescription = intent.getStringExtra("reminder_description")
        val hour = intent.getIntExtra("reminder_hour", -1)
        val minute = intent.getIntExtra("reminder_minute", -1)

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val timeTextView: TextView = findViewById(R.id.timeTextView)

        titleTextView.text = reminderTitle ?: "No Title"
        descriptionTextView.text = reminderDescription ?: "No Description"
        timeTextView.text = String.format("%02d:%02d", hour, minute)
    }
}
