package com.example.dailyboost

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private var tasks = mutableListOf<Task>()
    private lateinit var addTaskLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list) // Ensure the layout file name is correct

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewTasks)

        // Initialize the TaskAdapter with lambdas for deleting and editing tasks
        taskAdapter = TaskAdapter(tasks, { task -> deleteTask(task) }) { task ->
            openAddTaskActivity(task) // Open for editing
        }

        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<Task>("task")?.let { task ->
                    val existingTaskIndex = tasks.indexOfFirst { it.id == task.id }
                    if (existingTaskIndex != -1) {
                        tasks[existingTaskIndex] = task // Update existing task
                        taskAdapter.notifyItemChanged(existingTaskIndex) // Notify the adapter
                    } else {
                        tasks.add(task) // Add new task
                        taskAdapter.notifyItemInserted(tasks.size - 1) // Notify the adapter
                    }
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.fabAddTask).setOnClickListener {
            openAddTaskActivity(null) // No task to edit
        }
    }

    private fun openAddTaskActivity(task: Task?) {
        val intent = Intent(this, AddTaskActivity::class.java)
        task?.let {
            intent.putExtra("task", it) // Pass the task to edit
        }
        addTaskLauncher.launch(intent)
    }

    private fun deleteTask(task: Task) {
        val taskIndex = tasks.indexOf(task)
        if (taskIndex != -1) {
            tasks.removeAt(taskIndex)
            taskAdapter.notifyItemRemoved(taskIndex) // Notify the adapter of task removal
        }
    }
}
