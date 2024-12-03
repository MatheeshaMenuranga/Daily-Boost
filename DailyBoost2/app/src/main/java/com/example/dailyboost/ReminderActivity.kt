package com.example.dailyboost

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.TimePickerDialog
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {

    private lateinit var reminderTitleInput: EditText
    private lateinit var reminderDescriptionInput: EditText
    private lateinit var reminderTimeButton: Button
    private lateinit var setReminderButton: Button
    private var selectedHour: Int = -1
    private var selectedMinute: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        reminderTitleInput = findViewById(R.id.reminderTitleInput)
        reminderDescriptionInput = findViewById(R.id.reminderDescriptionInput)
        reminderTimeButton = findViewById(R.id.reminderTimeButton)
        setReminderButton = findViewById(R.id.setReminderButton)

        reminderTimeButton.setOnClickListener {
            showTimePicker()
        }

        setReminderButton.setOnClickListener {
            val title = reminderTitleInput.text.toString()
            val description = reminderDescriptionInput.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && selectedHour != -1 && selectedMinute != -1) {
                setReminder(title, description, selectedHour, selectedMinute)
                Toast.makeText(this, "Reminder Set for ${String.format("%02d:%02d", selectedHour, selectedMinute)}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a valid title, description, and time", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                this.selectedHour = selectedHour
                this.selectedMinute = selectedMinute
                reminderTimeButton.text = String.format("%02d:%02d", selectedHour, selectedMinute)
            }, hour, minute, true
        )
        timePickerDialog.show()
    }

    private fun setReminder(title: String, description: String, hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("reminder_title", title)
            putExtra("reminder_description", description)
            putExtra("reminder_hour", hour)
            putExtra("reminder_minute", minute)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
