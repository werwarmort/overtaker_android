package com.overtaker.app.ui.modifiers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.overtaker.app.ui.theme.BorderColor

@Composable
fun Modifier.defaultBlockSettings(): Modifier = this
    .clip(RoundedCornerShape(12.dp))
    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
    .padding(16.dp)
