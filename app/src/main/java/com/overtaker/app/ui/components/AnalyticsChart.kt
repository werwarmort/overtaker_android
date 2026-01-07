package com.overtaker.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
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
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val width = size.width
            val height = size.height
            val padding = 40f
            
            if (data.isEmpty()) return@Canvas
            
            val maxVal = (data.map { Math.max(it.value, it.penaltyValue) }.maxOrNull() ?: 10f).coerceAtLeast(10f)
            val xStep = (width - padding * 2) / (if (data.size > 1) data.size - 1 else 1)
            
            fun getY(value: Float) = height - padding - (value / maxVal) * (height - padding * 2)

            // Рисуем сетку (ось X)
            drawLine(Color.Gray.copy(alpha = 0.2f), Offset(padding, height - padding), Offset(width - padding, height - padding))

            // Рисуем линии
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
                
                // Точки
                drawCircle(color, 4.dp.toPx(), Offset(x, y))
                if (pt.penaltyValue > 0) drawCircle(penaltyColor, 3.dp.toPx(), Offset(x, py))
            }

            drawPath(mainPath, color, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round))
            drawPath(penaltyPath, penaltyColor, style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f)), cap = StrokeCap.Round))
        }
    }
}
