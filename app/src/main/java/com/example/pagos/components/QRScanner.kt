package com.example.pagos.components
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun QRScanner(
    onQrCodeScanned: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    var hasCameraPermission by remember { mutableStateOf(false) }
    var isScanning by remember { mutableStateOf(true) }

    // Lanzador de permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) onError("Permiso de cámara denegado.")
    }

    // Comprobar permisos al inicio
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            hasCameraPermission = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {
        Box(modifier = Modifier.fillMaxSize()) {
            var barcodeView: DecoratedBarcodeView? by remember { mutableStateOf(null) }

            AndroidView(
                factory = { ctx ->
                    DecoratedBarcodeView(ctx).apply {
                        // Modo escaneo continuo
                        decodeContinuous { result ->
                            if (isScanning) {
                                isScanning = false
                                onQrCodeScanned(result.text)
                            }
                        }
                        resume()
                        barcodeView = this
                    }
                },
                modifier = Modifier.fillMaxSize(),
                onRelease = { barcodeView?.pause() }
            )

            // Overlay visual
            ScannerOverlay()

            // Botón para pausar/reanudar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
            ) {
                Button(
                    onClick = {
                        if (isScanning) {
                            barcodeView?.pause()
                            isScanning = false
                        } else {
                            barcodeView?.resume()
                            isScanning = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isScanning) Color(0xFFD32F2F) else Color(0xFF388E3C)
                    )
                ) {
                    Text(if (isScanning) "Pausar" else "Reanudar")
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
            Text(
                text = "Se necesita permiso de cámara para escanear QR",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text("Conceder Permiso")
            }
        }
    }
}

@Composable
private fun ScannerOverlay() {
    val lineColor = Color(0xFF00E676)
    var lineOffset by remember { mutableStateOf(0f) }

    // Animar línea de escaneo
    LaunchedEffect(Unit) {
        while (true) {
            lineOffset += 6f
            delay(16L)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val squareSize = min(width, height) * 0.7f
        val left = (width - squareSize) / 2
        val top = (height - squareSize) / 2

        // Marco del escáner
        drawRoundRect(
            color = Color.Transparent,
            topLeft = Offset(left, top),
            size = Size(squareSize, squareSize),
            style = Stroke(width = 4.dp.toPx())
        )

        // Línea animada
        val yPos = top + (lineOffset % squareSize)
        drawLine(
            color = lineColor,
            start = Offset(left, yPos),
            end = Offset(left + squareSize, yPos),
            strokeWidth = 3.dp.toPx()
        )
    }
}
