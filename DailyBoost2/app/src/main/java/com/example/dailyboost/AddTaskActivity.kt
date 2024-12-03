package com.example.dailyboost

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dailyboost.databinding.ActivityAddTaskBinding

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private var isEditMode: Boolean = false
    private lateinit var currentTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Check if we are editing an existing task
        intent.getParcelableExtra<Task>("task")?.let { task ->
            isEditMode = true
            currentTask = task
            binding.titleInput.setText(task.title)
            binding.descriptionInput.setText(task.description)
            binding.saveButton.text = "Update Task" // Change button text
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = binding.titleInput.text.toString()
        val description = binding.descriptionInput.text.toString()
        val newTask = if (isEditMode) {
            currentTask.copy(title = title, description = description)
        } else {
            Task(System.currentTimeMillis(), title, description)
        }

        val intent = Intent().apply {
            putExtra("task", newTask)
        }
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun getTasksFromPreferences(): List<Task> {
        val sharedPreferences = getSharedPreferences("tasks", MODE_PRIVATE)
        val taskSet = sharedPreferences.getStringSet("task_keys", null)
        val tasks = mutableListOf<Task>()

        taskSet?.forEach { taskString ->
            val taskData = taskString.split("|")
            if (taskData.size == 3) {
                val id = taskData[0].toLong()
                val title = taskData[1]
                val description = taskData[2]
                tasks.add(Task(id, title, description))
            }
        }
        return tasks
    }
}
