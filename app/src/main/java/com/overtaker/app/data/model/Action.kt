package com.overtaker.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Action(
    val id: String? = null,
    val text: String,
    val points: Int,
    val isPenalty: Boolean,
    val createdAt: Long,
    val todoId: String? = null
)

@Serializable
data class ScoreResponse(
    val dayPoints: Int,
    val weekPoints: Int,
    val seasonPoints: Int
)
