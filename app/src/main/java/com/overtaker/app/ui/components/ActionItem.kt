package com.overtaker.app.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.overtaker.app.data.model.Action
import com.overtaker.app.ui.modifiers.defaultBlockSettings
import java.util.Calendar

@Composable
fun ActionItem(action: Action, onEdit: () -> Unit, onDelete: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    
    val isToday = run {
        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.DAY_OF_YEAR)
        val year = cal.get(Calendar.YEAR)
        cal.timeInMillis = action.createdAt
        today == cal.get(Calendar.DAY_OF_YEAR) && year == cal.get(Calendar.YEAR)
    }
    
    val isLinked = action.todoId != null
    val isLocked = isLinked || !isToday

    val hint = when {
        isLinked -> "Уберите отметку о выполнении в соответствующем разделе и отредактируйте там."
        !isToday -> "История прошлых дней не редактируется."
        else -> null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .defaultBlockSettings(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(
                text = if (action.isPenalty) "-${action.points}" else "+${action.points}",
                color = if (action.isPenalty) colorScheme.secondary else colorScheme.tertiary,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.width(45.dp)
            )
            Text(
                text = action.text,
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.primary
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    if (isLocked) Toast.makeText(context, hint, Toast.LENGTH_LONG).show()
                    else onEdit()
                }, 
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Default.Edit, 
                    contentDescription = null, 
                    tint = if (isLocked) Color.Gray else colorScheme.primary.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
            IconButton(
                onClick = {
                    if (isLocked) Toast.makeText(context, hint, Toast.LENGTH_LONG).show()
                    else onDelete()
                }, 
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Default.Close, 
                    contentDescription = null, 
                    tint = if (isLocked) Color.Gray else colorScheme.secondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
