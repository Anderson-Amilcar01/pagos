package com.example.pagos.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.unit.dp
import com.example.pagos.model.dasboardData.FlujoDiario

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewGraficos() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Datos de ejemplo para el gráfico de pastel
                val datosPie = mapOf(
                    "RECARGA" to 300.0,
                    "PAGO" to 180.0,
                    "COLEGIATURA" to 120.0,
                    "DEVOLUCION" to 60.0
                )

                PieChart(
                    data = datosPie,
                    title = "Distribución de Transacciones"
                )

                // Datos de ejemplo para el gráfico de barras
                val datosBar = listOf(
                    FlujoDiario("2025-09-30", ingresos = 400.0, gastos = 250.0),
                    FlujoDiario("2025-10-01", ingresos = 500.0, gastos = 320.0),
                    FlujoDiario("2025-10-02", ingresos = 280.0, gastos = 180.0),
                    FlujoDiario("2025-10-03", ingresos = 600.0, gastos = 200.0),
                )

                BarChart(
                    data = datosBar,
                    title = "Flujo Diario"
                )
            }
        }
    }
}