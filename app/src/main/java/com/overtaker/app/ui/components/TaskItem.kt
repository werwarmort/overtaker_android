package com.overtaker.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.overtaker.app.data.model.Task
import com.overtaker.app.ui.modifiers.defaultBlockSettings
import androidx.compose.foundation.background

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit) {
    val priorityColor = when (task.priority) {
        "low" -> Color(0xFF2CD32C)
        "high" -> Color(0xFFCC1E4A)
        else -> Color(0xFFFFC906)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .defaultBlockSettings()
    ) {
        // Цветная полоска приоритета
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(4.dp)
                .fillMaxHeight()
                .background(priorityColor)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() }
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "+${task.points}",
                color = Color(0xFF2CD32C),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
