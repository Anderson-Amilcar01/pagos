package com.example.pagos.screens
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import com.example.pagos.components.QRScanner
import com.example.pagos.components.QRCodeDisplay
import com.example.pagos.controllers.QrController
import com.example.pagos.controllers.UsuarioController

@Composable
fun QRCodeScreen(
    qrController: QrController,
    usuarioController: UsuarioController,
    token: String,
    usuarioId: Long
) {
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showScanner by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Pestañas para generar QR y escanear
        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Mi QR", "Escanear QR")

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                QRCodeDisplay(
                    bitmap = qrBitmap,
                    isLoading = isLoading,
                    onGenerateQR = {
                        isLoading = true
                        LaunchedEffect(Unit) {
                            val result = qrController.generarCodigoQr(token, usuarioId)
                            if (result.isSuccess) {
                                qrBitmap = result.getOrNull()
                            }
                            isLoading = false
                        }
                    }
                )
            }
            1 -> {
                if (showScanner) {
                    QRScanner(
                        onQrCodeScanned = { qrData ->
                            // Procesar QR escaneado (carnet del usuario)
                            showScanner = false
                            // Aquí puedes implementar la lógica para solicitar pago
                        },
                        onError = { error ->
                            // Manejar error
                            showScanner = false
                        }
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { showScanner = true }) {
                            Text("Iniciar Escáner QR")
                        }
                    }
                }
            }
        }
    }
}
