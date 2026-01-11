package com.overtaker.app.data.network

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import okhttp3.JavaNetCookieJar
import java.net.CookieManager
import java.net.CookiePolicy

object RetrofitClient {
    private const val BASE_URL = "https://overtaker.ru/api/"

    private val json = Json { ignoreUnknownKeys = true }

    // Создаем менеджер кук один раз, чтобы он хранил сессию
    private val cookieManager = CookieManager().apply {
        setCookiePolicy(CookiePolicy.ACCEPT_ALL)
    }

    fun getApiService(context: Context): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(cookieManager)) // Автоматическая обработка кук
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
