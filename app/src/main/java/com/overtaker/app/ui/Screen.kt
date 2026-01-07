package com.overtaker.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Tasks : Screen("tasks", "Задачи", Icons.Default.List)
    object Goals : Screen("goals", "Цели", Icons.Default.Star)
    object Logs : Screen("logs", "Логи", Icons.Default.DateRange)
    object Analytics : Screen("analytics", "Аналитика", Icons.Default.Info)
    object Account : Screen("account", "Аккаунт", Icons.Default.Person)
}
