package com.overtaker.app.data.network

import com.overtaker.app.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body body: Map<String, String>): Response<Unit>

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

    @POST("goals")
    suspend fun createGoal(@Body goal: Goal): Response<Unit>

    @PUT("goals/{id}")
    suspend fun updateGoal(@Path("id") id: Long, @Body goal: Goal): Response<Unit>

    @DELETE("goals/{id}")
    suspend fun deleteGoal(@Path("id") id: Long): Response<Unit>

    @GET("actions")
    suspend fun getActions(): List<Action>

    @POST("actions")
    suspend fun createAction(@Body action: Action): Response<Unit>

    @DELETE("actions/{id}")
    suspend fun deleteAction(@Path("id") id: String): Response<Unit>

    @GET("actions/score")
    suspend fun getScore(): ScoreResponse
}
