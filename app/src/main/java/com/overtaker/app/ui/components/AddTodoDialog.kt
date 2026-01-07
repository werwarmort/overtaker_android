package com.overtaker.app.ui.components

import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.overtaker.app.ui.modifiers.defaultBlockSettings

@Composable
fun AddTodoDialog(onDismiss: () -> Unit, onSave: (String, Int, String, String, List<String>) -> Unit) {
    var description by remember { mutableStateOf("") }
    var points by remember { mutableStateOf("0") }
    var priority by remember { mutableStateOf("medium") }
    var type by remember { mutableStateOf("task") }
    var subtasks by remember { mutableStateOf(listOf<String>()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        // Настройка размытия окна (для Android 12+)
        val view = LocalView.current
        DisposableEffect(view) {
            val window = (view.parent as? DialogWindowProvider)?.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                window?.let {
                    it.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                    it.attributes.blurBehindRadius = 60 // Интенсивность размытия
                }
            }
            onDispose {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .defaultBlockSettings()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Создать задачу", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = points,
                    onValueChange = { points = it },
                    label = { Text("Баллы") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Подзадачи", style = MaterialTheme.typography.titleSmall)
                subtasks.forEachIndexed { index, subtask ->
                    OutlinedTextField(
                        value = subtask,
                        onValueChange = {
                            val newList = subtasks.toMutableList()
                            newList[index] = it
                            subtasks = newList
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    )
                }
                TextButton(onClick = { subtasks = subtasks + "" }) {
                    Text("+ Добавить подзадачу")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSave(description, points.toIntOrNull() ?: 0, priority, type, subtasks.filter { it.isNotBlank() }); onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
