package com.overtaker.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overtaker.app.data.model.Action
import com.overtaker.app.ui.modifiers.defaultBlockSettings
import java.util.Calendar

@Composable
fun ActionItem(action: Action, onDelete: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    
    val isToday = run {
        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.DAY_OF_YEAR)
        val currentYear = cal.get(Calendar.YEAR)
        
        cal.timeInMillis = action.createdAt
        today == cal.get(Calendar.DAY_OF_YEAR) && currentYear == cal.get(Calendar.YEAR)
    }
    
    val isLocked = action.todoId != null || !isToday

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
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = action.text,
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.primary
            )
        }

        if (!isLocked) {
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, contentDescription = null, tint = colorScheme.secondary.copy(alpha = 0.6f))
            }
        }
    }
}
