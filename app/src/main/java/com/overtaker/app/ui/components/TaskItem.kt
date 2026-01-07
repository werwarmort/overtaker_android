package com.overtaker.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overtaker.app.data.model.Task
import com.overtaker.app.ui.modifiers.defaultBlockSettings
import java.util.Calendar

@Composable
fun TaskItem(
    task: Task, 
    onToggle: () -> Unit,
    onSubtaskToggle: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var isExpanded by remember { mutableStateOf(false) }
    
    val priorityColor = when (task.priority) {
        "low" -> colorScheme.tertiary
        "high" -> colorScheme.secondary
        else -> colorScheme.primary
    }

    val isCompletedToday = if (task.isCompleted && task.completedAt != null) {
        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.DAY_OF_YEAR)
        val currentYear = cal.get(Calendar.YEAR)
        
        cal.timeInMillis = task.completedAt
        today == cal.get(Calendar.DAY_OF_YEAR) && currentYear == cal.get(Calendar.YEAR)
    } else false
    
    val canToggle = !task.isCompleted || isCompletedToday

    Column(
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
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                if (task.subtasks.isNotEmpty()) {
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                }
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { onToggle() },
                    enabled = canToggle,
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
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!task.isCompleted) {
                    IconButton(onClick = onEdit, enabled = task.subgoalId == null) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(20.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = colorScheme.secondary, modifier = Modifier.size(20.dp))
                    }
                }
                Text(
                    text = "+${task.points}",
                    color = colorScheme.tertiary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(start = 32.dp, top = 8.dp)) {
                task.subtasks.forEach { sub ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = sub.isCompleted,
                            onCheckedChange = { onSubtaskToggle(sub.id) },
                            modifier = Modifier.size(24.dp),
                            enabled = !task.isCompleted,
                            colors = CheckboxDefaults.colors(
                                checkedColor = colorScheme.primary,
                                uncheckedColor = colorScheme.primary
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = sub.description,
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = if (sub.isCompleted) colorScheme.onSurface.copy(alpha = 0.6f) else colorScheme.primary
                        )
                    }
                }
            }
        }

        // Декоративная линия приоритета
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(24.dp)
                .background(priorityColor)
        )
    }
}
