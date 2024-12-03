package com.example.dailyboost

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button to navigate to Task List screen
        val taskListButton: Button = findViewById(R.id.btnTaskList)
        taskListButton.setOnClickListener {
            startActivity(Intent(this, TaskListActivity::class.java))
        }

        // Button to navigate to Timer screen
        val timerButton: Button = findViewById(R.id.btnTimer)
        timerButton.setOnClickListener {
            startActivity(Intent(this, TimerActivity::class.java))
        }

        // Button to navigate to Reminder screen
        val reminderButton: Button = findViewById(R.id.btnReminder)
        reminderButton.setOnClickListener {
            startActivity(Intent(this, ReminderActivity::class.java))
        }


    }
}
