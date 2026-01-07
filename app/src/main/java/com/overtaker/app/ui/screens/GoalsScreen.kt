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
import com.overtaker.app.data.model.Goal
import com.overtaker.app.ui.components.GoalItem
import com.overtaker.app.ui.components.AddGoalDialog
import com.overtaker.app.ui.viewmodel.GoalsViewModel

@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel, 
    onUpdate: () -> Unit,
    registerAddAction: (() -> Unit) -> Unit
) {
    var isCompletedExpanded by remember { mutableStateOf(false) }
    var isShowAddDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<Goal?>(null) }

    LaunchedEffect(Unit) {
        registerAddAction { isShowAddDialog = true }
    }

    val activeGoals = viewModel.goals.filter { !it.isCompleted }
    val completedGoals = viewModel.goals.filter { it.isCompleted }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(activeGoals) { goal ->
                    GoalItem(
                        goal = goal,
                        onToggle = { viewModel.toggleGoal(goal); onUpdate() },
                        onSubgoalToggle = { sub -> viewModel.toggleSubgoal(goal, sub); onUpdate() },
                        onSendToTasks = { sub -> viewModel.sendToTasks(goal, sub); onUpdate() },
                        onEdit = { editingGoal = goal },
                        onDelete = { viewModel.deleteGoal(goal.id!!); onUpdate() }
                    )
                }

                if (completedGoals.isNotEmpty()) {
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
                                text = "Выполненные (${completedGoals.size})",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }

                    if (isCompletedExpanded) {
                        items(completedGoals) { goal ->
                            GoalItem(
                                goal = goal,
                                onToggle = { viewModel.toggleGoal(goal); onUpdate() },
                                onSubgoalToggle = { sub -> viewModel.toggleSubgoal(goal, sub); onUpdate() },
                                onSendToTasks = { sub -> viewModel.sendToTasks(goal, sub); onUpdate() },
                                onEdit = { editingGoal = goal },
                                onDelete = { viewModel.deleteGoal(goal.id!!); onUpdate() }
                            )
                        }
                    }
                }
            }
        }

        if (isShowAddDialog || editingGoal != null) {
            AddGoalDialog(
                initialGoal = editingGoal,
                onDismiss = { 
                    isShowAddDialog = false
                    editingGoal = null
                },
                onSave = { title, subgoals ->
                    if (editingGoal != null) {
                        viewModel.updateGoal(editingGoal!!.copy(title = title, subgoals = subgoals))
                    } else {
                        viewModel.addGoal(title, null, subgoals)
                    }
                    onUpdate()
                }
            )
        }
    }
}
