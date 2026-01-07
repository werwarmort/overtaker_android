package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.model.Goal
import com.overtaker.app.data.model.GoalSubgoal
import com.overtaker.app.data.model.Action
import com.overtaker.app.data.model.Task
import com.overtaker.app.data.network.RetrofitClient
import kotlinx.coroutines.launch

class GoalsViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)

    var goals by mutableStateOf<List<Goal>>(emptyList())
    var isLoading by mutableStateOf(false)

    init {
        fetchGoals()
    }

    fun fetchGoals(onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            isLoading = true
            try {
                goals = apiService.getGoals()
                onComplete?.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addGoal(title: String, description: String?, subgoals: List<GoalSubgoal>, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val newGoal = Goal(
                    title = title,
                    description = description,
                    isCompleted = false,
                    createdAt = System.currentTimeMillis(),
                    subgoals = subgoals
                )
                apiService.createGoal(newGoal)
                fetchGoals(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateGoal(goal: Goal, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                apiService.updateGoal(goal.id!!, goal)
                fetchGoals(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteGoal(id: Long, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                apiService.deleteGoal(id)
                fetchGoals(onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun toggleGoal(goal: Goal, onComplete: () -> Unit) {
        viewModelScope.launch {
            updateGoal(goal.copy(isCompleted = !goal.isCompleted), onComplete)
        }
    }

    fun toggleSubgoal(goal: Goal, subgoal: GoalSubgoal, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val newStatus = !subgoal.isCompleted
                var actionId = subgoal.completedActionId

                if (newStatus) {
                    val newActionId = System.currentTimeMillis().toString()
                    val action = Action(
                        id = newActionId,
                        text = "Выполнена подцель: ${subgoal.description}",
                        points = subgoal.points,
                        isPenalty = false,
                        createdAt = System.currentTimeMillis()
                    )
                    apiService.createAction(action)
                    actionId = newActionId
                } else {
                    if (subgoal.completedActionId != null) {
                        apiService.deleteAction(subgoal.completedActionId)
                        actionId = null
                    }
                }

                val updatedSubgoals = goal.subgoals.map {
                    if (it.id == subgoal.id) it.copy(isCompleted = newStatus, completedActionId = actionId) else it
                }
                updateGoal(goal.copy(subgoals = updatedSubgoals), onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun sendToTasks(goal: Goal, subgoal: GoalSubgoal, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val newTask = Task(
                    description = subgoal.description,
                    points = subgoal.points,
                    priority = "medium",
                    type = "task",
                    isCompleted = false,
                    createdAt = System.currentTimeMillis(),
                    subgoalId = subgoal.id
                )
                apiService.createTask(newTask)
                
                val updatedSubgoals = goal.subgoals.map {
                    if (it.id == subgoal.id) it.copy(isSentToTasks = true) else it
                }
                updateGoal(goal.copy(subgoals = updatedSubgoals), onComplete)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
