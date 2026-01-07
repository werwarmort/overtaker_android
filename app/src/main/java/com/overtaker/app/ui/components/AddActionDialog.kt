package com.overtaker.app.ui.components

import android.os.Build
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.overtaker.app.data.model.Action
import com.overtaker.app.ui.modifiers.defaultBlockSettings

@Composable
fun AddActionDialog(initialAction: Action? = null, onDismiss: () -> Unit, onSave: (String, Int, Boolean) -> Unit) {
    var text by remember { mutableStateOf(initialAction?.text ?: "") }
    var points by remember { mutableStateOf(initialAction?.points?.toString() ?: "") }
    var isPenalty by remember { mutableStateOf(initialAction?.isPenalty ?: false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val view = LocalView.current
        DisposableEffect(view) {
            val window = (view.parent as? DialogWindowProvider)?.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                window?.let {
                    it.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                    it.attributes.blurBehindRadius = 60
                }
            }
            onDispose {}
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .defaultBlockSettings()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(if (initialAction == null) "Добавить запись" else "Редактировать", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text("Описание") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = points,
                        onValueChange = { newValue ->
                            // Если вводим число и текущее значение 0 или пусто, заменяем его
                            points = if (newValue.startsWith("0") && newValue.length > 1) {
                                newValue.substring(1)
                            } else {
                                newValue
                            }
                        },
                        label = { Text("Баллы") },
                        modifier = Modifier.weight(1f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isPenalty, onCheckedChange = { isPenalty = it })
                        Text("Штраф", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onSave(text, points.toIntOrNull() ?: 0, isPenalty); onDismiss() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
