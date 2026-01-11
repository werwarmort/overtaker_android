package com.overtaker.app.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.overtaker.app.ui.Screen
import com.overtaker.app.ui.viewmodel.TasksViewModel
import com.overtaker.app.ui.viewmodel.GoalsViewModel
import com.overtaker.app.ui.viewmodel.ActionsViewModel
import com.overtaker.app.ui.viewmodel.AnalyticsViewModel
import com.overtaker.app.ui.viewmodel.ScoreViewModel
import com.overtaker.app.ui.components.GlobalHeader

@Composable
fun MainScreen(onLogout: () -> Unit, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scoreViewModel = remember { ScoreViewModel(context) }
    
    LaunchedEffect(Unit) {
        scoreViewModel.fetchScore()
    }

    var onAddClickAction by remember { mutableStateOf<(() -> Unit)?>(null) }

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
        },
        floatingActionButton = {
            if (onAddClickAction != null) {
                FloatingActionButton(
                    onClick = { onAddClickAction?.invoke() },
                    shape = CircleShape, // Делаем кнопку круглой
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        val showHeader = currentRoute != Screen.Account.route && currentRoute != Screen.Analytics.route

        Column(modifier = Modifier.padding(innerPadding)) {
            if (showHeader) {
                GlobalHeader(
                    day = scoreViewModel.dayPoints,
                    week = scoreViewModel.weekPoints,
                    season = scoreViewModel.seasonPoints
                )
            }

            NavHost(navController, startDestination = Screen.Tasks.route) {
                composable(Screen.Tasks.route) {
                    TasksScreen(
                        viewModel = remember { TasksViewModel(context) }, 
                        onUpdate = { scoreViewModel.fetchScore() },
                        registerAddAction = { onAddClickAction = it }
                    )
                }
                composable(Screen.Goals.route) {
                    GoalsScreen(
                        viewModel = remember { GoalsViewModel(context) },
                        onUpdate = { scoreViewModel.fetchScore() },
                        registerAddAction = { onAddClickAction = it }
                    )
                }
                composable(Screen.Logs.route) { 
                    LogsScreen(
                        viewModel = remember { ActionsViewModel(context) },
                        onUpdate = { scoreViewModel.fetchScore() },
                        registerAddAction = { onAddClickAction = null }
                    )
                }
                composable(Screen.Analytics.route) {
                    onAddClickAction = null
                    AnalyticsScreen(viewModel = remember { AnalyticsViewModel(context) })
                }
                composable(Screen.Account.route) { 
                    onAddClickAction = null
                    AccountScreen(
                        onLogout = onLogout, 
                        isDarkTheme = isDarkTheme, 
                        onThemeChange = onThemeChange
                    ) 
                }
            }
        }
    }
}
