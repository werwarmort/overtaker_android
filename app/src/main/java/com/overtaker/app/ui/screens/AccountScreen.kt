package com.overtaker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.overtaker.app.data.network.SessionManager
import com.overtaker.app.ui.modifiers.defaultBlockSettings

@Composable
fun AccountScreen(onLogout: () -> Unit, isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Настройки аккаунта", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 32.dp))
        
        // Блок переключения темы
        Row(
            modifier = Modifier.fillMaxWidth().defaultBlockSettings(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Темная тема", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { onThemeChange(it) }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                sessionManager.clearAuthToken()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Выйти из аккаунта")
        }
    }
}
