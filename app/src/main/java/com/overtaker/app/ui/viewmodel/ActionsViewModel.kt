package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.model.Action
import com.overtaker.app.data.network.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ActionsViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)

    var actions by mutableStateOf<List<Action>>(emptyList())
    var isLoading by mutableStateOf(false)

    init {
        fetchActions()
    }

    fun fetchActions() {
        viewModelScope.launch {
            isLoading = true
            try {
                actions = apiService.getActions()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun addAction(text: String, points: Int, isPenalty: Boolean) {
        viewModelScope.launch {
            try {
                val action = Action(
                    text = text,
                    points = points,
                    isPenalty = isPenalty,
                    createdAt = System.currentTimeMillis()
                )
                apiService.createAction(action)
                fetchActions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteAction(id: String) {
        viewModelScope.launch {
            try {
                apiService.deleteAction(id)
                fetchActions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getGroupedActions(): Map<String, List<Action>> {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return actions.groupBy { sdf.format(Date(it.createdAt)) }
    }
}
