package com.overtaker.app.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import com.overtaker.app.data.model.Goal
import com.overtaker.app.data.model.GoalSubgoal
import com.overtaker.app.ui.modifiers.defaultBlockSettings

@Composable
fun GoalItem(
    goal: Goal,
    onToggle: () -> Unit,
    onSubgoalToggle: (GoalSubgoal) -> Unit,
    onSendToTasks: (GoalSubgoal) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var isExpanded by remember { mutableStateOf(false) }

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
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = colorScheme.primary
                    )
                }
                Checkbox(
                    checked = goal.isCompleted,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(checkedColor = colorScheme.primary)
                )
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (goal.isCompleted) Color.Gray else colorScheme.primary
                )
            }
            
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = colorScheme.secondary, modifier = Modifier.size(20.dp))
                }
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(start = 32.dp, top = 8.dp)) {
                goal.subgoals.forEach { sub ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Checkbox(
                                checked = sub.isCompleted,
                                onCheckedChange = { onSubgoalToggle(sub) },
                                enabled = !sub.isSentToTasks,
                                colors = CheckboxDefaults.colors(checkedColor = colorScheme.primary)
                            )
                            Text(
                                text = sub.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (sub.isCompleted) Color.Gray else colorScheme.primary
                            )
                        }
                        
                        if (sub.isSentToTasks) {
                            Text("В задачах", fontSize = 10.sp, color = colorScheme.primary.copy(alpha = 0.5f))
                        } else if (!sub.isCompleted) {
                            IconButton(onClick = { onSendToTasks(sub) }, modifier = Modifier.size(24.dp)) {
                                Text("🚀", fontSize = 16.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
