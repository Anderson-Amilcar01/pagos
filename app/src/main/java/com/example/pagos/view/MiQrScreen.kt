package com.example.pagos.view
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@Composable
fun MiQrScreen(navController: NavController) {
    // En una app real, generarías un QR con el carnet del usuario
    val userCarnet = "CARNET12345" // Esto debería venir del usuario logueado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Mi Código QR",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Simular QR (en producción usarías ZXing para generarlo)
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.White)
                .border(2.dp, Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.QrCode,
                    contentDescription = "QR Code",
                    modifier = Modifier.size(120.dp)
                )
                Text("Carnet: $userCarnet")
                Text("(QR simulado)")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Comparte este código QR para recibir pagos",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}