package com.overtaker.app.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.overtaker.app.data.network.SessionManager
import com.overtaker.app.ui.Screen
import com.overtaker.app.ui.viewmodel.TasksViewModel

@Composable
fun MainScreen(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val items = listOf(
        Screen.Tasks,
        Screen.Goals,
        Screen.Logs,
        Screen.Analytics,
        Screen.Account
    )

    Scaffold(
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    
                    for (screen in items) {
                        val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null) },
                            label = { Text(screen.title, maxLines = 1) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Tasks.route, Modifier.padding(innerPadding)) {
            composable(Screen.Tasks.route) { TasksScreen(TasksViewModel(context)) }
            composable(Screen.Goals.route) { Box(Modifier.fillMaxSize()) { Text("Глобальные цели") } }
            composable(Screen.Logs.route) { Box(Modifier.fillMaxSize()) { Text("Логи") } }
            composable(Screen.Analytics.route) { Box(Modifier.fillMaxSize()) { Text("Аналитика") } }
            composable(Screen.Account.route) { AccountScreen(onLogout) }
        }
    }
}
