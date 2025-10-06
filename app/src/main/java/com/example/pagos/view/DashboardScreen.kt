package com.example.pagos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pagos.model.Transaccion
import com.example.pagos.viewmodel.CarteraState
import com.example.pagos.viewmodel.CarteraViewModel
import androidx.compose.ui.unit.times
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val viewModel: CarteraViewModel = viewModel()
    val carteraState = viewModel.carteraState.value

    // Simular userId (debería venir del login)
    val userId = 1L

    LaunchedEffect(Unit) {
        viewModel.loadCartera(userId)
        viewModel.loadTransacciones()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Cartera") },
                actions = {
                    IconButton(onClick = { navController.navigate("mi_qr") }) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Mi QR"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("dashboard") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("recarga") },
                    icon = { Icon(Icons.Default.AttachMoney, contentDescription = "Recargar") },
                    label = { Text("Recargar") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("pago") },
                    icon = { Icon(Icons.Default.Payment, contentDescription = "Pagar") },
                    label = { Text("Pagar") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("historial") },
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (carteraState) {
                is CarteraState.Success -> {
                    // Tarjeta de saldo
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Saldo Actual",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                "$${carteraState.cartera.saldo}",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Gráfico de transacciones recientes
                    TransaccionesChart(viewModel.transaccionesState.value)

                    // Acciones rápidas
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionButton(
                            icon = Icons.Default.QrCodeScanner,
                            text = "Escanear QR",
                            onClick = { navController.navigate("qr_scanner") }
                        )
                        ActionButton(
                            icon = Icons.Default.QrCode,
                            text = "Mi QR",
                            onClick = { navController.navigate("mi_qr") }
                        )
                    }
                }
                is CarteraState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CarteraState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(carteraState.message, color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun TransaccionesChart(transacciones: List<Transaccion>) {
    val chartEntries = remember(transacciones) {
        transacciones.takeLast(7).mapIndexed { index, transaccion ->
            ChartEntry(
                x = index.toFloat(),
                y = transaccion.monto.toFloat(),
                color = if (transaccion.tipo == "RECARGA") Color.Green else Color.Red
            )
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Últimas Transacciones",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (chartEntries.isNotEmpty()) {
                // Gráfico simple de barras
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    chartEntries.forEach { entry ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(entry.y * 2.dp)
                                    .background(entry.color)
                            )
                            Text(
                                "$${entry.y.toInt()}",
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            } else {
                Text("No hay transacciones recientes")
            }
        }
    }
}

data class ChartEntry(val x: Float, val y: Float, val color: Color)

@Composable
fun ActionButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(
            icon,
            contentDescription = text,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text, style = MaterialTheme.typography.labelSmall)
    }
}
