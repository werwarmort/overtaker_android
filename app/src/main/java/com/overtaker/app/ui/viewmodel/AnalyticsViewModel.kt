package com.overtaker.app.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overtaker.app.data.model.Action
import com.overtaker.app.data.network.RetrofitClient
import kotlinx.coroutines.launch
import java.util.*

data class ChartDataPoint(
    val label: String,
    val value: Float,
    val penaltyValue: Float = 0f
)

class AnalyticsViewModel(context: Context) : ViewModel() {
    private val apiService = RetrofitClient.getApiService(context)
    
    var weekData by mutableStateOf<List<ChartDataPoint>>(emptyList())
    var seasonData by mutableStateOf<List<ChartDataPoint>>(emptyList())
    var yearData by mutableStateOf<List<ChartDataPoint>>(emptyList())
    var isLoading by mutableStateOf(false)

    init {
        fetchData()
    }

    fun fetchData() {
        viewModelScope.launch {
            isLoading = true
            try {
                val actions = apiService.getActions()
                processData(actions)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    private fun processData(actions: List<Action>) {
        val cal = Calendar.getInstance()
        
        // 1. Неделя (последние 7 дней)
        val week = mutableListOf<ChartDataPoint>()
        for (i in 6 downTo 0) {
            cal.timeInMillis = System.currentTimeMillis()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dayStart = getStartOfDay(cal.timeInMillis)
            val dayEnd = dayStart + 86400000
            
            val dayActions = actions.filter { it.createdAt in dayStart until dayEnd }
            week.add(ChartDataPoint(
                label = getDayName(cal.get(Calendar.DAY_OF_WEEK)),
                value = dayActions.filter { !it.isPenalty }.sumOf { it.points }.toFloat(),
                penaltyValue = dayActions.filter { it.isPenalty }.sumOf { it.points }.toFloat()
            ))
        }
        weekData = week

        // 2. Сезон (3 недели)
        val season = mutableListOf<ChartDataPoint>()
        for (i in 2 downTo 0) {
            val weekEnd = System.currentTimeMillis() - (i * 7L * 86400000L)
            val weekStart = weekEnd - (6L * 86400000L)
            val weekActions = actions.filter { it.createdAt in weekStart..weekEnd }
            
            season.add(ChartDataPoint(
                label = "Нед ${3-i}",
                value = weekActions.filter { !it.isPenalty }.sumOf { it.points }.toFloat(),
                penaltyValue = weekActions.filter { it.isPenalty }.sumOf { it.points }.toFloat()
            ))
        }
        seasonData = season

        // 3. Год (12 месяцев)
        val year = mutableListOf<ChartDataPoint>()
        for (i in 11 downTo 0) {
            cal.timeInMillis = System.currentTimeMillis()
            cal.add(Calendar.MONTH, -i)
            val month = cal.get(Calendar.MONTH)
            val curYear = cal.get(Calendar.YEAR)
            
            val monthActions = actions.filter {
                val aCal = Calendar.getInstance().apply { timeInMillis = it.createdAt }
                aCal.get(Calendar.MONTH) == month && aCal.get(Calendar.YEAR) == curYear
            }
            
            year.add(ChartDataPoint(
                label = getMonthName(month),
                value = monthActions.filter { !it.isPenalty }.sumOf { it.points }.toFloat(),
                penaltyValue = monthActions.filter { it.isPenalty }.sumOf { it.points }.toFloat()
            ))
        }
        yearData = year
    }

    private fun getStartOfDay(ts: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = ts
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private fun getDayName(dayOfWeek: Int) = when(dayOfWeek) {
        Calendar.MONDAY -> "Пн"; Calendar.TUESDAY -> "Вт"; Calendar.WEDNESDAY -> "Ср"
        Calendar.THURSDAY -> "Чт"; Calendar.FRIDAY -> "Пт"; Calendar.SATURDAY -> "Сб"
        else -> "Вс"
    }

    private fun getMonthName(month: Int) = when(month) {
        0 -> "Янв"; 1 -> "Фев"; 2 -> "Мар"; 3 -> "Апр"; 4 -> "Май"; 5 -> "Июн"
        6 -> "Июл"; 7 -> "Авг"; 8 -> "Сен"; 9 -> "Окт"; 10 -> "Ноя"; else -> "Дек"
    }
}
