package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.model.Task
import com.overtaker.app.data.model.Subtask
import com.overtaker.app.data.model.Action
import com.overtaker.app.data.network.RetrofitClient
import kotlinx.coroutines.launch

class TasksViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)

    var tasks by mutableStateOf<List<Task>>(emptyList())
    var isLoading by mutableStateOf(false)

    init {
        fetchData()
    }

    fun fetchData(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            isLoading = true
            try {
                tasks = apiService.getTasks()
                onComplete?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addTask(description: String, points: Int, priority: String, type: String, subtasks: List<Subtask>, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val newTask = Task(
                    description = description,
                    points = points,
                    priority = priority,
                    type = type,
                    isCompleted = false,
                    createdAt = System.currentTimeMillis(),
                    subtasks = subtasks
                )
                apiService.createTask(newTask)
                fetchData(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTask(task: Task, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                apiService.updateTask(task.id!!, task)
                fetchData(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleTask(task: Task, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val newStatus = !task.isCompleted
                var actionId = task.completedActionId

                if (newStatus) {
                    // 1. Создаем лог на бэкенде
                    val newActionId = System.currentTimeMillis().toString()
                    val action = Action(
                        id = newActionId,
                        text = "Выполнена задача: ${task.description}",
                        points = task.points,
                        isPenalty = false,
                        createdAt = System.currentTimeMillis(),
                        todoId = task.id.toString()
                    )
                    apiService.createAction(action)
                    actionId = newActionId
                } else {
                    // 2. Удаляем лог если отменяем выполнение
                    if (task.completedActionId != null) {
                        apiService.deleteAction(task.completedActionId)
                        actionId = null
                    }
                }

                // 3. Обновляем саму задачу
                val updatedTask = task.copy(
                    isCompleted = newStatus,
                    completedAt = if (newStatus) System.currentTimeMillis() else null,
                    completedActionId = actionId
                )
                apiService.updateTask(task.id!!, updatedTask)
                
                fetchData(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleSubtask(task: Task, subtaskId: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val subtask = task.subtasks.find { it.id == subtaskId } ?: return@launch
                
                var actionId = subtask.completedActionId
                val newStatus = !subtask.isCompleted

                if (newStatus) {
                    val newActionId = System.currentTimeMillis().toString()
                    val action = Action(
                        id = newActionId,
                        text = "Выполнена подзадача: ${subtask.description}",
                        points = 0,
                        isPenalty = false,
                        createdAt = System.currentTimeMillis(),
                        todoId = task.id.toString()
                    )
                    apiService.createAction(action)
                    actionId = newActionId
                } else {
                    if (subtask.completedActionId != null) {
                        apiService.deleteAction(subtask.completedActionId)
                        actionId = null
                    }
                }

                val updatedSubtasks = task.subtasks.map {
                    if (it.id == subtaskId) it.copy(isCompleted = newStatus, completedActionId = actionId) else it
                }
                val updatedTask = task.copy(subtasks = updatedSubtasks)
                apiService.updateTask(task.id!!, updatedTask)
                fetchData(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTask(id: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                apiService.deleteTask(id)
                fetchData(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
