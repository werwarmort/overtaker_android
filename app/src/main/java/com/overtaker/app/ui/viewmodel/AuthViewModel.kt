package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.network.RetrofitClient
import com.overtaker.app.data.network.SessionManager
import kotlinx.coroutines.launch

class AuthViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)
    private val sessionManager = SessionManager(context)

    var isLogin by mutableStateOf(true)
    var username by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)

    fun onAuthClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                if (isLogin) {
                    val response = apiService.login(mapOf("username" to username, "password" to password))
                    if (response.isSuccessful && response.body() != null) {
                        sessionManager.saveAuthToken(response.body()!!.token)
                        isSuccess = true
                        onSuccess()
                    } else {
                        errorMessage = "Ошибка входа: ${response.code()}"
                    }
                } else {
                    val response = apiService.register(mapOf(
                        "username" to username, 
                        "email" to email, 
                        "password" to password
                    ))
                    if (response.isSuccessful) {
                        isLogin = true // Переключаем на вход после регистрации
                        errorMessage = "Регистрация успешна! Войдите."
                    } else {
                        errorMessage = "Ошибка регистрации: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Неизвестная ошибка"
            } finally {
                isLoading = false
            }
        }
    }
}
