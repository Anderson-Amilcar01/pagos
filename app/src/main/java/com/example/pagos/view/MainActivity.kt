package com.example.pagos.view
import androidx.compose.foundation.layout.fillMaxSize
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pagos.ui.PagosAppTheme
import com.example.pagos.view.DashboardScreen
//import com.example.pagos.view.HistorialScreen
import com.example.pagos.view.LoginScreen
import com.example.pagos.view.MiQrScreen
//import com.example.pagos.view.PagoScreen
import com.example.pagos.view.QrScannerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagosAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PagosApp()
                }
            }
        }
    }
}

@Composable
fun PagosApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("recarga") { RecargaScreen(navController) }
       // composable("pago") { PagoScreen(navController) }
        composable("qr_scanner") { QrScannerScreen(navController) }
        composable("mi_qr") { MiQrScreen(navController) }
        //composable("historial") { HistorialScreen(navController) }
    }
}