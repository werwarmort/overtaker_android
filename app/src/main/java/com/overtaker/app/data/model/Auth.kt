package com.overtaker.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val id: Long,
    val username: String,
    val email: String
)

@Serializable
data class UserInfo(
    val id: Long,
    val username: String,
    val email: String
)
