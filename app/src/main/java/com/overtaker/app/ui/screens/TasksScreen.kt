package com.overtaker.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.overtaker.app.data.model.Task
import com.overtaker.app.ui.components.TaskItem
import com.overtaker.app.ui.components.AddTodoDialog
import com.overtaker.app.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(
    viewModel: TasksViewModel, 
    onUpdate: () -> Unit,
    registerAddAction: (() -> Unit) -> Unit
) {
    var isCompletedExpanded by remember { mutableStateOf(false) }
    var isShowAddDialog by remember { mutableStateOf(false) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    
    LaunchedEffect(Unit) {
        registerAddAction { isShowAddDialog = true }
    }

    val activeTasks = viewModel.tasks.filter { !it.isCompleted }
    val completedTasks = viewModel.tasks.filter { it.isCompleted }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(activeTasks) { task ->
                    TaskItem(
                        task = task, 
                        onToggle = { 
                            viewModel.toggleTask(task, onUpdate)
                        },
                        onSubtaskToggle = { subId -> 
                            viewModel.toggleSubtask(task, subId, onUpdate)
                        },
                        onEdit = { editingTask = task },
                        onDelete = { 
                            viewModel.deleteTask(task.id!!, onUpdate)
                        }
                    )
                }

                if (completedTasks.isNotEmpty()) {
                    item {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isCompletedExpanded = !isCompletedExpanded }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isCompletedExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Выполненные (${completedTasks.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    if (isCompletedExpanded) {
                        items(completedTasks) { task ->
                            TaskItem(
                                task = task, 
                                onToggle = { 
                                    viewModel.toggleTask(task, onUpdate)
                                },
                                onSubtaskToggle = { subId -> 
                                    viewModel.toggleSubtask(task, subId, onUpdate)
                                },
                                onEdit = { editingTask = task },
                                onDelete = { 
                                    viewModel.deleteTask(task.id!!, onUpdate)
                                }
                            )
                        }
                    }
                }
            }
        }

        if (isShowAddDialog || editingTask != null) {
            AddTodoDialog(
                initialTask = editingTask,
                onDismiss = { 
                    isShowAddDialog = false 
                    editingTask = null
                },
                onSave = { desc, pts, priority, type, subs ->
                    if (editingTask != null) {
                        viewModel.updateTask(editingTask!!.copy(
                            description = desc, 
                            points = pts, 
                            priority = priority, 
                            type = type, 
                            subtasks = subs
                        ), onUpdate)
                    } else {
                        viewModel.addTask(desc, pts, priority, type, subs, onUpdate)
                    }
                }
            )
        }
    }
}
