package com.example.dailyboost

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews

class TaskWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetComponent = ComponentName(context, TaskWidgetProvider::class.java)
            val widgetIds = appWidgetManager.getAppWidgetIds(widgetComponent)
            for (widgetId in widgetIds) {
                updateAppWidget(context, appWidgetManager, widgetId)
            }
        }

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val sharedPreferences = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
            val taskSet = sharedPreferences.getStringSet("task_keys", null)
            val tasksText = taskSet?.joinToString("\n") { taskString ->
                val taskData = taskString.split("|")
                if (taskData.size == 3) "${taskData[1]}: ${taskData[2]}" else "Invalid Task Format"
            } ?: "No tasks available"

            // Update the widget view
            val views = RemoteViews(context.packageName, R.layout.widget_layout).apply {
                setTextViewText(R.id.widget_text, tasksText)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
