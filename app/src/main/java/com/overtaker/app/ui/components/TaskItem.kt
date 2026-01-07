package com.overtaker.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.overtaker.app.data.model.Task
import com.overtaker.app.ui.modifiers.defaultBlockSettings

@Composable
fun TaskItem(task: Task, onToggle: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    
    val priorityColor = when (task.priority) {
        "low" -> colorScheme.tertiary
        "high" -> colorScheme.secondary
        else -> colorScheme.primary
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .defaultBlockSettings()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorScheme.primary,
                        uncheckedColor = colorScheme.primary
                    )
                )
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (task.isCompleted) colorScheme.onSurface.copy(alpha = 0.6f) else colorScheme.primary
                )
            }
            Text(
                text = "+${task.points}",
                color = colorScheme.tertiary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Декоративная линия приоритета
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(4.dp)
                .height(24.dp)
                .background(priorityColor)
        )
    }
}
