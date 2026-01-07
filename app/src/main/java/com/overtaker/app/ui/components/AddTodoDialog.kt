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
import com.overtaker.app.data.model.Task
import com.overtaker.app.data.model.Subtask
import com.overtaker.app.ui.modifiers.defaultBlockSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    initialTask: Task? = null,
    onDismiss: () -> Unit, 
    onSave: (String, Int, String, String, List<Subtask>) -> Unit
) {
    var description by remember { mutableStateOf(initialTask?.description ?: "") }
    var points by remember { mutableStateOf(initialTask?.points?.toString() ?: "") }
    var priority by remember { mutableStateOf(initialTask?.priority ?: "medium") }
    var type by remember { mutableStateOf(initialTask?.type ?: "task") }
    var subtasks by remember { mutableStateOf(initialTask?.subtasks ?: emptyList()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val view = LocalView.current
        DisposableEffect(view) {
            val window = (view.parent as? DialogWindowProvider)?.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                window?.let {
                    it.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                    it.attributes.blurBehindRadius = 60
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
                Text(
                    text = if (initialTask == null) "Создать задачу" else "Редактировать", 
                    style = MaterialTheme.typography.titleLarge
                )
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
                    onValueChange = { newValue ->
                        points = if (newValue.startsWith("0") && newValue.length > 1) newValue.substring(1) else newValue
                    },
                    label = { Text("Баллы") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
                
                Text("Срочность", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("low" to "Низкая", "medium" to "Средняя", "high" to "Высокая").forEach { (id, label) ->
                        FilterChip(
                            selected = priority == id,
                            onClick = { priority = id },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Подзадачи", style = MaterialTheme.typography.titleSmall)
                subtasks.forEachIndexed { index, subtask ->
                    OutlinedTextField(
                        value = subtask.description,
                        onValueChange = {
                            val newList = subtasks.toMutableList()
                            newList[index] = newList[index].copy(description = it)
                            subtasks = newList
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    )
                }
                TextButton(onClick = {
                    subtasks = subtasks + Subtask(
                        id = System.currentTimeMillis().toString() + Math.random().toString(),
                        description = "",
                        isCompleted = false
                    )
                }) {
                    Text("+ Добавить подзадачу")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        onSave(
                            description, 
                            points.toIntOrNull() ?: 0, 
                            priority, 
                            type, 
                            subtasks.filter { it.description.isNotBlank() }
                        )
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
