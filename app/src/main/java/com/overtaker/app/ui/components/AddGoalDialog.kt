package com.overtaker.app.ui.components

import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import com.overtaker.app.data.model.Goal
import com.overtaker.app.data.model.GoalSubgoal

@Composable
fun AddGoalDialog(
    initialGoal: Goal? = null,
    onDismiss: () -> Unit,
    onSave: (String, List<GoalSubgoal>) -> Unit
) {
    var title by remember { mutableStateOf(initialGoal?.title ?: "") }
    var subgoals by remember { mutableStateOf(initialGoal?.subgoals ?: emptyList()) }

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
            window?.setDimAmount(0.6f)
            onDispose {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = if (initialGoal == null) "Создать цель" else "Редактировать", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название цели") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Подцели", style = MaterialTheme.typography.titleSmall)
                subgoals.forEachIndexed { index, sub ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = sub.description,
                            onValueChange = {
                                val newList = subgoals.toMutableList()
                                newList[index] = newList[index].copy(description = it)
                                subgoals = newList
                            },
                            label = { Text("Описание") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = sub.points.toString(),
                            onValueChange = { newValue ->
                                val cleanValue = if (newValue.startsWith("0") && newValue.length > 1) newValue.substring(1) else newValue
                                val newList = subgoals.toMutableList()
                                newList[index] = newList[index].copy(points = cleanValue.toIntOrNull() ?: 0)
                                subgoals = newList
                            },
                            label = { Text("Баллы") },
                            modifier = Modifier.width(80.dp)
                        )
                        IconButton(onClick = {
                             subgoals = subgoals.filterIndexed { i, _ -> i != index }
                        }) {
                             Icon(Icons.Default.Clear, contentDescription = "Delete subgoal", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
                TextButton(onClick = {
                    subgoals = subgoals + GoalSubgoal(
                        id = System.currentTimeMillis().toString() + Math.random().toString(),
                        description = "",
                        isCompleted = false,
                        isSentToTasks = false,
                        points = 0
                    )
                }) {
                    Text("+ Добавить подцель")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSave(title, subgoals.filter { it.description.isNotBlank() }); onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
