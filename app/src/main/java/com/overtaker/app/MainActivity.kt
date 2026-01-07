package com.overtaker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.overtaker.app.data.network.SessionManager
import com.overtaker.app.ui.screens.AuthScreen
import com.overtaker.app.ui.screens.MainScreen
import com.overtaker.app.ui.theme.OvertakerTheme
import com.overtaker.app.ui.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)

        setContent {
            OvertakerTheme {
                val navController = rememberNavController()
                val startDestination = if (sessionManager.fetchAuthToken() != null) "main" else "auth"

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("auth") {
                        AuthScreen(AuthViewModel(this@MainActivity)) {
                            navController.navigate("main") {
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    }
                    composable("main") {
                        MainScreen(onLogout = {
                            navController.navigate("auth") {
                                popUpTo("main") { inclusive = true }
                            }
                        })
                    }
                }
            }
        }
    }
}
