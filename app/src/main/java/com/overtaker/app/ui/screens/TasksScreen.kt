package com.overtaker.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.overtaker.app.ui.components.TaskItem
import com.overtaker.app.ui.viewmodel.TasksViewModel

@Composable
fun TasksScreen(viewModel: TasksViewModel) {
    var isCompletedExpanded by remember { mutableStateOf(false) }
    val activeTasks = viewModel.tasks.filter { !it.isCompleted }
    val completedTasks = viewModel.tasks.filter { it.isCompleted }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Баллы за день: ${viewModel.dayPoints}",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(activeTasks) { task ->
                TaskItem(task = task, onToggle = { viewModel.toggleTask(task) })
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
                        TaskItem(task = task, onToggle = { viewModel.toggleTask(task) })
                    }
                }
            }
        }
    }
}
