package com.overtaker.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Goal(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean,
    val createdAt: Long,
    val subgoals: List<GoalSubgoal> = emptyList()
)

@Serializable
data class GoalSubgoal(
    val id: String,
    val description: String,
    val isCompleted: Boolean,
    val isSentToTasks: Boolean,
    val points: Int,
    val completedActionId: String? = null
)
