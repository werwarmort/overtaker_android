package com.overtaker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overtaker.app.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel, onAuthSuccess: () -> Unit) {
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )

    // ОБЕРТКА SURFACE: Она закрасит фон правильным цветом темы
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background 
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (viewModel.isLogin) "Вход" else "Регистрация",
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Имя пользователя") },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            if (!viewModel.isLogin) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Пароль") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (viewModel.isLoading) {
                CircularProgressIndicator()
                // Исправляем цвет индикатора на фоне
                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Button(
                    onClick = { viewModel.onAuthClick(onAuthSuccess) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(if (viewModel.isLogin) "Войти" else "Зарегистрироваться")
                }

                TextButton(onClick = { viewModel.isLogin = !viewModel.isLogin }) {
                    Text(if (viewModel.isLogin) "Нет аккаунта? Создать" else "Уже есть аккаунт? Войти")
                }
            }
        }
    }
}
