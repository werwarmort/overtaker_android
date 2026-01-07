package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.network.RetrofitClient
import kotlinx.coroutines.launch

class ScoreViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)

    var dayPoints by mutableStateOf(0)
    var weekPoints by mutableStateOf(0)
    var seasonPoints by mutableStateOf(0)

    init {
        fetchScore()
    }

    fun fetchScore() {
        viewModelScope.launch {
            try {
                val score = apiService.getScore()
                dayPoints = score.dayPoints
                weekPoints = score.weekPoints
                seasonPoints = score.seasonPoints
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
