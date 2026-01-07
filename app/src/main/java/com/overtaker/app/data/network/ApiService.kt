package com.overtaker.app.data.network

import com.overtaker.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<String>

    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Response<Unit>

    @PUT("tasks/{id}")
    suspend fun updateTask(@Path("id") id: Long, @Body task: Task): Response<Unit>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<Unit>

    @GET("goals")
    suspend fun getGoals(): List<Goal>

    @GET("actions")
    suspend fun getActions(): List<Action>

    @GET("actions/score")
    suspend fun getScore(): ScoreResponse
}
