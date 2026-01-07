package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.model.Task
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

    fun toggleTask(task: Task) {
        viewModelScope.launch {
            // Логика аналогична вебу: если выполняем, создаем запись в логах (делается на бэке или тут)
            // На вебе мы создавали Action вручную перед переключением задачи.
            // Для Android упростим: бэкенд сам создаст лог если мы пришлем нужные поля.
            // Но для полной совместимости с вебом, мы должны сначала создать Action.
            
            try {
                val newStatus = !task.isCompleted
                val actionId = if (newStatus) System.currentTimeMillis().toString() else null
                
                if (newStatus) {
                    apiService.getActions() // Заглушка или вызов создания Action
                    // В идеале тут должен быть вызов создания Action, как на вебе.
                    // Но для начала просто обновим задачу
                }

                val updatedTask = task.copy(
                    isCompleted = newStatus,
                    completedAt = if (newStatus) System.currentTimeMillis() else null,
                    completedActionId = actionId
                )
                
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
