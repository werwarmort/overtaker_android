package com.overtaker.app.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.overtaker.app.ui.viewmodel.ChartDataPoint

@Composable
fun AnalyticsChart(
    title: String,
    data: List<ChartDataPoint>,
    color: Color,
    penaltyColor: Color = Color(0xFFCC1E4A)
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Text(
            text = title, 
            style = MaterialTheme.typography.titleMedium, 
            color = colorScheme.primary, 
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Canvas(modifier = Modifier.fillMaxWidth().height(250.dp)) {
            val width = size.width
            val height = size.height
            val padding = 50f
            val chartHeight = height - padding * 2
            val chartWidth = width - padding * 2
            
            if (data.isEmpty()) return@Canvas

            // 1. Рисуем светлый бэкграунд
            drawRoundRect(
                color = colorScheme.primary.copy(alpha = 0.05f),
                size = size,
                cornerRadius = CornerRadius(12.dp.toPx())
            )
            
            val maxVal = (data.map { Math.max(it.value, it.penaltyValue) }.maxOrNull() ?: 10f).coerceAtLeast(10f)
            val xStep = chartWidth / (if (data.size > 1) data.size - 1 else 1)
            
            fun getY(value: Float) = height - padding - (value / maxVal) * chartHeight

            // Настройка кисти для текста через nativeCanvas
            val paint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 10.sp.toPx()
                this.color = android.graphics.Color.parseColor("#" + colorScheme.primary.toArgb().toUInt().toString(16).substring(2))
                typeface = Typeface.DEFAULT_BOLD
            }
            
            val penaltyPaint = Paint().apply {
                textAlign = Paint.Align.CENTER
                textSize = 9.sp.toPx()
                this.color = android.graphics.Color.parseColor("#" + penaltyColor.toArgb().toUInt().toString(16).substring(2))
            }

            val mainPath = Path()
            val penaltyPath = Path()
            
            data.forEachIndexed { i, pt ->
                val x = padding + i * xStep
                val y = getY(pt.value)
                val py = getY(pt.penaltyValue)
                
                if (i == 0) {
                    mainPath.moveTo(x, y)
                    penaltyPath.moveTo(x, py)
                } else {
                    mainPath.lineTo(x, y)
                    penaltyPath.lineTo(x, py)
                }
                
                // Рисуем точки
                drawCircle(color, 4.dp.toPx(), Offset(x, y))
                if (pt.penaltyValue > 0) drawCircle(penaltyColor, 3.dp.toPx(), Offset(x, py))

                // 2. Рисуем значения рядом с точками
                drawContext.canvas.nativeCanvas.drawText(
                    pt.value.toInt().toString(), 
                    x, 
                    y - 12.dp.toPx(), 
                    paint
                )
                
                if (pt.penaltyValue > 0) {
                    drawContext.canvas.nativeCanvas.drawText(
                        "-${pt.penaltyValue.toInt()}", 
                        x, 
                        py + 15.dp.toPx(), 
                        penaltyPaint
                    )
                }

                // Подписи осей (дни/месяцы)
                drawContext.canvas.nativeCanvas.drawText(
                    pt.label, 
                    x, 
                    height - 10.dp.toPx(), 
                    penaltyPaint.apply { this.color = android.graphics.Color.GRAY }
                )
            }

            drawPath(mainPath, color, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            drawPath(penaltyPath, penaltyColor, style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)), cap = StrokeCap.Round))
        }
    }
}
