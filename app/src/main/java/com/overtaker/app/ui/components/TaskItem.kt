package com.overtaker.app.ui.components

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    
    val priorityColor = when (task.priority) {
        "low" -> Color(0xFF2CD32C)
        "high" -> Color(0xFFCC1E4A)
        else -> Color(0xFFFFC906)
    }

    val isCompletedToday = if (task.isCompleted && task.completedAt != null) {
        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.DAY_OF_YEAR)
        cal.timeInMillis = task.completedAt
        today == cal.get(Calendar.DAY_OF_YEAR)
    } else false
    
    val canToggle = !task.isCompleted || isCompletedToday
    val isLinkedToGoal = task.subgoalId != null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .defaultBlockSettings(borderColor = priorityColor) // Красим бордер в цвет приоритета
    ) {
        // Тонкая акцентная полоска слева (как в вебе border-left: 6px)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(6.dp)
                .fillMaxHeight()
                .padding(vertical = 8.dp) // Чуть короче высоты карточки для изящности
                .background(priorityColor, androidx.compose.foundation.shape.CircleShape)
        )

        Column(modifier = Modifier.fillMaxWidth().padding(start = 12.dp)) {
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
                    OvertakerCheckbox(
                        checked = task.isCompleted,
                        onCheckedChange = { onToggle() },
                        enabled = canToggle
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (task.isCompleted) colorScheme.primary.copy(alpha = 0.6f) else colorScheme.primary
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!task.isCompleted) {
                        IconButton(
                            onClick = {
                                if (isLinkedToGoal) {
                                    Toast.makeText(context, "Редактирование доступно в разделе целей", Toast.LENGTH_LONG).show()
                                } else {
                                    onEdit()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit, 
                                contentDescription = null, 
                                tint = if (isLinkedToGoal) Color.Gray else colorScheme.primary, 
                                modifier = Modifier.size(20.dp)
                            )
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
                Column(modifier = Modifier.padding(start = 32.dp, top = 8.dp, bottom = 8.dp)) {
                    task.subtasks.forEach { sub ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OvertakerCheckbox(
                                checked = sub.isCompleted,
                                onCheckedChange = { onSubtaskToggle(sub.id) },
                                enabled = !task.isCompleted
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = sub.description,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 14.sp,
                                color = if (sub.isCompleted) colorScheme.primary.copy(alpha = 0.6f) else colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
