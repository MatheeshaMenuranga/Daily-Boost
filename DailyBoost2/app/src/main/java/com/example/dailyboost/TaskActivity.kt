package com.example.dailyboost

import android.annotation.SuppressLint
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private var tasks = mutableListOf<Task>()
    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        taskAdapter = TaskAdapter(tasks, { task -> deleteTask(task) }) { task -> openAddTaskActivity(task) }
        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTasksFromPreferences() // Load tasks when activity starts

        addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<Task>("task")?.let { task ->
                    val existingTaskIndex = tasks.indexOfFirst { it.id == task.id }
                    if (existingTaskIndex != -1) {
                        tasks[existingTaskIndex] = task // Update existing task
                        taskAdapter.notifyItemChanged(existingTaskIndex)
                    } else {
                        tasks.add(task) // Add new task
                        taskAdapter.notifyItemInserted(tasks.size - 1)
                    }
                    saveTasksToPreferences() // Save updated task list
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener { openAddTaskActivity(null) }
    }

    private fun openAddTaskActivity(task: Task?) {
        val intent = Intent(this, AddTaskActivity::class.java)
        task?.let { intent.putExtra("task", it) }
        addTaskLauncher.launch(intent)
    }

    private fun deleteTask(task: Task) {
        val taskIndex = tasks.indexOf(task)
        if (taskIndex != -1) {
            tasks.removeAt(taskIndex)
            taskAdapter.notifyItemRemoved(taskIndex)
            saveTasksToPreferences()
        }
    }

    private fun saveTasksToPreferences() {
        val sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val taskSet = tasks.map { "${it.id}|${it.title}|${it.description}" }.toSet()
        editor.putStringSet("task_keys", taskSet)
        editor.apply()
        updateWidget() // Trigger widget update after saving tasks
    }

    private fun loadTasksFromPreferences() {
        val sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val taskSet = sharedPreferences.getStringSet("task_keys", null)
        taskSet?.forEach { taskString ->
            val taskData = taskString.split("|")
            if (taskData.size == 3) {
                tasks.add(Task(taskData[0].toLong(), taskData[1], taskData[2]))
            }
        }
        taskAdapter.notifyDataSetChanged()
    }

    private fun updateWidget() {
        TaskWidgetProvider.updateAllWidgets(this)
    }
}
