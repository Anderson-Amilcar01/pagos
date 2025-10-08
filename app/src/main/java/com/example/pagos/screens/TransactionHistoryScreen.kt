package com.example.pagos.screens
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pagos.components.PieChart
import com.example.pagos.components.TransactionHistory
import com.example.pagos.model.Transaccion
import com.example.pagos.controllers.TransaccionController

@Composable
fun TransactionHistoryScreen(
    transaccionController: TransaccionController,
    token: String,
    usuarioId: Long
) {
    var transacciones by remember { mutableStateOf<List<Transaccion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val result = transaccionController.obtenerTransaccionesUsuario(token, usuarioId)
            if (result.isSuccess) {
                transacciones = result.getOrNull() ?: emptyList()
            } else {
                errorMessage = "Error al obtener transacciones"
            }
        } catch (e: Exception) {
            errorMessage = "Error inesperado: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            errorMessage != null -> {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            else -> {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    if (transacciones.isNotEmpty()) {
                        // Gráfico de pastel por tipo de transacción
                        val transaccionesPorTipo = transacciones.groupBy { it.tipo.name }
                            .mapValues { (_, transacciones) ->
                                transacciones.sumOf { it.monto }
                            }

                        PieChart(
                            data = transaccionesPorTipo,
                            title = "Distribución por Tipo"
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    TransactionHistory(transacciones = transacciones)
                }
            }
        }
    }
}
