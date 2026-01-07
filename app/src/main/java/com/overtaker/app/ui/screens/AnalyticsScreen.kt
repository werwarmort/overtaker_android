package com.overtaker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.overtaker.app.ui.components.AnalyticsChart
import com.overtaker.app.ui.viewmodel.AnalyticsViewModel

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Аналитика", style = MaterialTheme.typography.headlineMedium, color = colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        AnalyticsChart(
            title = "Очки за неделю",
            data = viewModel.weekData,
            color = colorScheme.primary
        )

        AnalyticsChart(
            title = "Очки за сезон",
            data = viewModel.seasonData,
            color = colorScheme.tertiary
        )

        AnalyticsChart(
            title = "Очки за год",
            data = viewModel.yearData,
            color = colorScheme.secondary
        )
    }
}
