package com.overtaker.app.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun GlobalHeader(day: Int, week: Int, season: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatBox(label = "день", value = day, modifier = Modifier.weight(1f))
        StatBox(label = "неделя", value = week, modifier = Modifier.weight(1f))
        StatBox(label = "сезон", value = season, modifier = Modifier.weight(1f))
    }
}

@Composable
fun StatBox(label: String, value: Int, modifier: Modifier = Modifier) {
    var showHint by remember { mutableStateOf(false) }
    
    LaunchedEffect(showHint) {
        if (showHint) {
            delay(5000)
            showHint = false
        }
    }

    Box(
        modifier = modifier
            .height(40.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f), RoundedCornerShape(10.dp))
            .clickable { showHint = true },
        contentAlignment = Alignment.Center
    ) {
        Crossfade(targetState = showHint || value == 0) { isLabelMode ->
            if (isLabelMode) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (value == 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary,
                    fontWeight = if (value == 0) FontWeight.Normal else FontWeight.Bold
                )
            } else {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
