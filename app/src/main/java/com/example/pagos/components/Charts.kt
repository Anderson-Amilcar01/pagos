package com.example.pagos.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pagos.model.dasboardData.*
import kotlin.math.min

@Composable
fun PieChart(
    data: Map<String, Double>,
    title: String,
    modifier: Modifier = Modifier
) {
    val total = data.values.sum()
    var startAngle = 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .size(220.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = min(size.width, size.height) / 2 * 0.8f
                val rectSize = Size(radius * 2, radius * 2)
                val topLeft = Offset(
                    (size.width - rectSize.width) / 2,
                    (size.height - rectSize.height) / 2
                )

                data.forEach { (label, value) ->
                    val sweepAngle = (value / total * 360).toFloat()
                    drawArc(
                        color = getColorForLabel(label),
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = topLeft,
                        size = rectSize,
                        style = Fill
                    )
                    startAngle += sweepAngle
                }
            }

            // Total en el centro
            Text(
                text = "$${"%.2f".format(total)}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Leyenda
        Column(
            modifier = Modifier.padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            data.forEach { (label, value) ->
                LegendItem(label, value, total)
            }
        }
    }
}

@Composable
fun BarChart(
    data: List<FlujoDiario>,
    title: String,
    modifier: Modifier = Modifier
) {
    val maxValue = data.maxOfOrNull { maxOf(it.ingresos, it.gastos) } ?: 0.0
    val barWidth = 24.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            data.forEach { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Barras apiladas
                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height((item.ingresos / maxValue * 140).dp)
                            .background(Color(0xFF4CAF50))
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .width(barWidth)
                            .height((item.gastos / maxValue * 140).dp)
                            .background(Color(0xFFF44336))
                    )

                    Text(
                        text = item.fecha.substring(5), // Día-mes
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Leyenda
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            LegendItem("Ingresos", Color(0xFF4CAF50))
            Spacer(modifier = Modifier.width(16.dp))
            LegendItem("Gastos", Color(0xFFF44336))
        }
    }
}

@Composable
fun LegendItem(label: String, value: Double, total: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(getColorForLabel(label), shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$label: ${(value / total * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
        )
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
    }
}

private fun getColorForLabel(label: String): Color {
    return when (label.uppercase()) {
        "RECARGA" -> Color(0xFF4CAF50)
        "PAGO" -> Color(0xFFF44336)
        "COLEGIATURA" -> Color(0xFF2196F3)
        "DEVOLUCION" -> Color(0xFFFF9800)
        else -> Color(0xFF9E9E9E)
    }
}
