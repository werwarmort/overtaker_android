package com.overtaker.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overtaker.app.data.model.Action
import com.overtaker.app.ui.components.ActionItem
import com.overtaker.app.ui.components.AddActionDialog
import com.overtaker.app.ui.viewmodel.ActionsViewModel

@Composable
fun LogsScreen(viewModel: ActionsViewModel, onUpdate: () -> Unit, registerAddAction: (() -> Unit) -> Unit) {
    var isShowAddDialog by remember { mutableStateOf(false) }
    var editingAction by remember { mutableStateOf<Action?>(null) }
    val groupedActions = viewModel.getGroupedActions()

    LaunchedEffect(Unit) {
        registerAddAction { isShowAddDialog = true }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            groupedActions.forEach { (date, actions) ->
                val dayTotal = actions.sumOf { if (it.isPenalty) -it.points else it.points }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(text = date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = "Итог: ${if (dayTotal >= 0) "+" else ""}$dayTotal",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                }

                items(actions) { action ->
                    ActionItem(
                        action = action, 
                        onEdit = { editingAction = action },
                        onDelete = { 
                            viewModel.deleteAction(action.id!!)
                            onUpdate()
                        }
                    )
                }
            }
        }

        if (isShowAddDialog || editingAction != null) {
            AddActionDialog(
                initialAction = editingAction,
                onDismiss = { 
                    isShowAddDialog = false
                    editingAction = null
                },
                onSave = { text, pts, isPenalty ->
                    if (editingAction != null) {
                        viewModel.updateAction(editingAction!!.id!!, text, pts, isPenalty)
                    } else {
                        viewModel.addAction(text, pts, isPenalty)
                    }
                    onUpdate()
                }
            )
        }
    }
}
