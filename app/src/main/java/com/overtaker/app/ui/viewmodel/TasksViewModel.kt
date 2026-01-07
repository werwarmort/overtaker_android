package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.model.Task
import com.overtaker.app.data.model.Subtask
import com.overtaker.app.data.network.RetrofitClient
import kotlinx.coroutines.launch

class TasksViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)

    var tasks by mutableStateOf<List<Task>>(emptyList())
    var dayPoints by mutableStateOf(0)
    var isLoading by mutableStateOf(false)

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            isLoading = true
            try {
                tasks = apiService.getTasks()
                val score = apiService.getScore()
                dayPoints = score.dayPoints
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addTask(description: String, points: Int, priority: String, type: String, subtasks: List<String>) {
        viewModelScope.launch {
            try {
                val newTask = Task(
                    description = description,
                    points = points,
                    priority = priority,
                    type = type,
                    isCompleted = false,
                    createdAt = System.currentTimeMillis(),
                    subtasks = subtasks.map { Subtask(id = System.currentTimeMillis().toString() + Math.random(), description = it, isCompleted = false) }
                )
                apiService.createTask(newTask)
                fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                apiService.updateTask(task.id!!, task)
                fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            try {
                val newStatus = !task.isCompleted
                val updatedTask = task.copy(isCompleted = newStatus)
                apiService.updateTask(task.id!!, updatedTask)
                fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleSubtask(task: Task, subtaskId: String) {
        viewModelScope.launch {
            try {
                val updatedSubtasks = task.subtasks.map {
                    if (it.id == subtaskId) it.copy(isCompleted = !it.isCompleted) else it
                }
                val updatedTask = task.copy(subtasks = updatedSubtasks)
                apiService.updateTask(task.id!!, updatedTask)
                fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch {
            try {
                apiService.deleteTask(id)
                fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
