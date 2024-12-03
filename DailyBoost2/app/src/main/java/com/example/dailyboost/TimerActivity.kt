package com.example.dailyboost

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private lateinit var timerDisplay: TextView
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var resetButton: Button

    private var timer: CountDownTimer? = null
    private var timeInMillis: Long = 0L
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timerDisplay = findViewById(R.id.timerDisplay)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener { startTimer() }
        stopButton.setOnClickListener { stopTimer() }
        resetButton.setOnClickListener { resetTimer() }
    }

    private fun startTimer() {
        if (!isRunning) {
            isRunning = true
            timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeInMillis += 1000
                    updateTimerDisplay()
                }

                override fun onFinish() {}
            }.start()
        }
    }

    private fun stopTimer() {
        if (isRunning) {
            timer?.cancel()
            isRunning = false
        }
    }

    private fun resetTimer() {
        stopTimer()
        timeInMillis = 0L
        updateTimerDisplay()
    }

    private fun updateTimerDisplay() {
        val hours = (timeInMillis / 3600000)
        val minutes = (timeInMillis % 3600000) / 60000
        val seconds = (timeInMillis % 60000) / 1000
        timerDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
