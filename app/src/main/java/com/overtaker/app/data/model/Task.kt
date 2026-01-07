package com.overtaker.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long? = null,
    val description: String,
    val points: Int,
    val priority: String,
    val isCompleted: Boolean,
    val createdAt: Long,
    val type: String,
    val completedAt: Long? = null,
    val completedActionId: String? = null,
    val subgoalId: String? = null,
    val subtasks: List<Subtask> = emptyList()
)

@Serializable
data class Subtask(
    val id: String,
    val description: String,
    val isCompleted: Boolean
)
