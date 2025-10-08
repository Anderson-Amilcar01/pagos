package com.example.pagos.components
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
//import com.journeyapps.barcodescanner.BarcodeScannerView
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.min

@Composable
fun QRScanner(
    onQrCodeScanned: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Solicitar permisos de cámara
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    val barcodeView = BarcodeScannerView(ctx).apply {
                        decodeContinuous { result ->
                            onQrCodeScanned(result.text)
                        }
                    }
                    barcodeView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Overlay para ayudar al escaneo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val squareSize = minOf(width, height) * 0.6f

                    // Dibujar marco del QR
                    drawRect(
                        color = Color.Transparent,
                        topLeft = Offset((width - squareSize) / 2, (height - squareSize) / 2),
                        size = Size(squareSize, squareSize),
                        style = Stroke(width = 4.dp.toPx())
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Se necesita permiso de cámara para escanear QR")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Solicitar permiso (implementar lógica de permisos)
            }) {
                Text("Conceder Permiso")
            }
        }
    }
}
