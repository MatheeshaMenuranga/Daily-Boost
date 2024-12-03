package com.example.dailyboost

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: List<Task>,
    private val onDeleteTask: (Task) -> Unit, // Lambda for deleting tasks
    private val onEditTask: (Task) -> Unit // Lambda for editing tasks
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription) // Assuming you added this
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        private val editButton: Button = itemView.findViewById(R.id.editButton)

        fun bind(task: Task) {
            taskTitle.text = task.title
            taskDescription.text = task.description // Set the description
            deleteButton.setOnClickListener {
                onDeleteTask(task) // Call the lambda to delete the task
            }
            editButton.setOnClickListener {
                onEditTask(task) // Call the lambda to edit the task
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_task, parent, false) // Corrected to item_task
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun update(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}
