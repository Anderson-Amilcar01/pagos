package com.example.pagos.ui.scanner

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.zxing.integration.android.*

@Composable
fun QRCodeScannerScreen() {
    val context = LocalContext.current
    val activity = context as Activity

    var scanResult by remember { mutableStateOf<String?>(null) }

    // Lanzador para recibir el resultado del escaneo
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(
            result.resultCode,
            result.data
        )
        if (intentResult != null) {
            scanResult = intentResult.contents ?: "No se detectó ningún código."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Escáner de Código QR",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val integrator = IntentIntegrator(activity)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            integrator.setPrompt("Escanea el código QR")
            integrator.setCameraId(0)
            integrator.setBeepEnabled(true)
            integrator.setBarcodeImageEnabled(false)
            val intent = integrator.createScanIntent()
            scanLauncher.launch(intent)
        }) {
            Text("Iniciar escaneo")
        }

        Spacer(modifier = Modifier.height(24.dp))

        scanResult?.let {
            Text(
                text = "Resultado: $it",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
