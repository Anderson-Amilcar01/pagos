package com.example.pagos.screens
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import com.example.pagos.controllers.DashboardController
import com.example.pagos.model.dasboardData.*
import com.example.pagos.components.BarChart
import com.example.pagos.components.PieChart

@Composable
fun DashboardScreen(
    dashboardController: DashboardController,
    token: String
) {
    var estadisticas by remember { mutableStateOf<EstadisticasGenerales?>(null) }
    var flujoDiario by remember { mutableStateOf<List<FlujoDiario>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val statsResult = dashboardController.obtenerEstadisticasGenerales(token)
            val flujoResult = dashboardController.obtenerFlujoUltimos7Dias(token)

            if (statsResult.isSuccess) estadisticas = statsResult.getOrNull()
            else errorMessage = "Error al obtener estadísticas."

            if (flujoResult.isSuccess) flujoDiario = flujoResult.getOrNull() ?: emptyList()
            else errorMessage = "Error al obtener flujo diario."

        } catch (e: Exception) {
            errorMessage = "Error inesperado: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    item {
                        estadisticas?.let { stats ->
                            AnimatedVisibility(visible = true) {
                                EstadisticasCards(estadisticas = stats)
                            }
                        }
                    }

                    item {
                        estadisticas?.let { stats ->
                            val datosPie = mapOf(
                                "Usuarios" to stats.totalUsuariosActivos.toDouble(),
                                "Ingresos" to stats.ingresosTotales,
                                "Transacciones" to stats.transaccionesHoy.toDouble(),
                                "Nuevos" to stats.nuevosUsuariosMes.toDouble()
                            )
                            AnimatedVisibility(visible = true) {
                                PieChart(
                                    data = datosPie,
                                    title = "Distribución de Métricas"
                                )
                            }
                        }
                    }

                    item {
                        if (flujoDiario.isNotEmpty()) {
                            AnimatedVisibility(visible = true) {
                                BarChart(
                                    data = flujoDiario,
                                    title = "Flujo de los Últimos 7 Días"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadisticasCards(estadisticas: EstadisticasGenerales) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EstadisticaCard(
                title = "Usuarios Activos",
                value = estadisticas.totalUsuariosActivos.toString(),
                color = Color(0xFF2196F3)
            )
            EstadisticaCard(
                title = "Ingresos Totales",
                value = "$${String.format("%.2f", estadisticas.ingresosTotales)}",
                color = Color(0xFF4CAF50)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EstadisticaCard(
                title = "Transacciones Hoy",
                value = estadisticas.transaccionesHoy.toString(),
                color = Color(0xFFFF9800)
            )
            EstadisticaCard(
                title = "Nuevos Usuarios",
                value = estadisticas.nuevosUsuariosMes.toString(),
                color = Color(0xFF9C27B0)
            )
        }
    }
}

@Composable
fun EstadisticaCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(110.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}
