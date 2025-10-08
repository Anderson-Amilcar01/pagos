package com.example.pagos.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pagos.controllers.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.*

sealed class MainTab(val route: String, val title: String) {
    object Dashboard : MainTab("main/dashboard", "Dashboard")
    object QR : MainTab("main/qr", "QR Code")
    object Transactions : MainTab("main/transactions", "Transacciones")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    authToken: String,
    currentUser: com.example.pagos.model.Usuario,
    usuarioController: UsuarioController,
    transaccionController: TransaccionController,
    qrController: QrController,
    dashboardController: DashboardController,
    onLogout: () -> Unit
) {
    val innerNavController = rememberNavController()

    val items = listOf(
        MainTab.Dashboard,
        MainTab.QR,
        MainTab.Transactions
    )

    Scaffold(
        topBar = {
          CenterAlignedTopAppBar(
              title = { Text("MarketCup") },
              actions = {
                  IconButton(onClick = onLogout) {
                      Icon(Icons.Default.QrCode, contentDescription = "Cerrar Sesión")
                  }
              }
          )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = innerNavController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { tab ->
                    NavigationBarItem(
                        icon = {
                            when (tab) {
                                MainTab.Dashboard -> Icon(Icons.Default.Dashboard, contentDescription = tab.title)
                                MainTab.QR -> Icon(Icons.Default.QrCode, contentDescription = tab.title)
                                MainTab.Transactions -> Icon(Icons.Default.History, contentDescription = tab.title)
                            }
                        },
                        label = { Text(tab.title) },
                        selected = currentRoute == tab.route,
                        onClick = {
                            innerNavController.navigate(tab.route) {
                                // evita duplicar destinos en backstack
                                popUpTo(innerNavController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = innerNavController,
            startDestination = MainTab.Dashboard.route,
            modifier = androidx.compose.ui.Modifier.padding(paddingValues)
        ) {
            composable(MainTab.Dashboard.route) {
                DashboardScreen(dashboardController = dashboardController, token = authToken)
            }
            composable(MainTab.QR.route) {
                QRCodeScreen(qrController = qrController, usuarioController = usuarioController, token = authToken, usuarioId = currentUser.idUsuario)
            }
            composable(MainTab.Transactions.route) {
                TransactionHistoryScreen(transaccionController = transaccionController, token = authToken, usuarioId = currentUser.idUsuario)
            }
        }
    }
}
