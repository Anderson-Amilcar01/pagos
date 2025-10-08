package com.example.pagos.navigation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.*
import com.example.pagos.controllers.*
import com.example.pagos.model.Usuario
import com.example.pagos.service.ApiService
import com.example.pagos.screens.LoginScreen
import com.example.pagos.screens.MainScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")
    object QRScanner : Screen("qr_scanner") // ejemplo para rutas adicionales
    // añade más rutas si las necesitas
}

@Composable
fun MarketCupApp(apiService: ApiService) {
    val navController = rememberNavController()

    // Guardar token/usuario de forma que sobrevivan rotaciones (saveable)
    var authToken by rememberSaveable { mutableStateOf<String?>(null) }
    var currentUser by rememberSaveable { mutableStateOf<Usuario?>(null) }

    // Controladores centrales (puedes cambiarlos por Hilt/ViewModel más adelante)
    val authController = remember { AuthController(apiService) }
    val usuarioController = remember { UsuarioController(apiService) }
    val transaccionController = remember { TransaccionController(apiService) }
    val qrController = remember { QrController(apiService) }
    val dashboardController = remember { DashboardController(apiService) }

    NavHost(navController = navController, startDestination = Screen.Login.route) {

        composable(Screen.Login.route) {
            LoginScreen(
                authController = authController,
                onLoginSuccess = { token, usuario ->
                    authToken = token
                    currentUser = usuario
                    // navegar a main y limpiar backstack
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            // Si no hay token, redirigir a login (seguridad básica)
            if (authToken == null || currentUser == null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                }
            } else {
                MainScreen(
                    navController = navController, // ahora MainScreen usa NavController interno
                    authToken = authToken!!,
                    currentUser = currentUser!!,
                    usuarioController = usuarioController,
                    transaccionController = transaccionController,
                    qrController = qrController,
                    dashboardController = dashboardController,
                    onLogout = {
                        authToken = null
                        currentUser = null
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Main.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Ejemplo: ruta para un scanner dedicado (si necesitas pantalla full screen)
        composable(Screen.QRScanner.route) {
            // ... QR scanner screen composable si la quieres independiente
        }
    }
}
