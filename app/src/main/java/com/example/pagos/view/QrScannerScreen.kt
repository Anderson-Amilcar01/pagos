package com.example.pagos.view
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pagos.viewmodel.CarteraViewModel
import com.example.pagos.viewmodel.PagoState
@Composable
fun QrScannerScreen(navController: NavController) {
    val context = LocalContext.current
    var scannedCode by remember { mutableStateOf<String?>(null) }
    val viewModel: CarteraViewModel = viewModel()

    // Simular permisos de cámara (deberías implementar manejo real de permisos)
    val hasCameraPermission by remember { mutableStateOf(true) }

    if (!hasCameraPermission) {
        Text("Se necesita permiso de cámara")
        return
    }

    if (scannedCode != null) {
        // Mostrar formulario de pago después de escanear
        PagoFormScreen(scannedCode!!, navController, viewModel)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            // Aquí integrarías un lector de QR real como ZXing
            // Por ahora simulamos el escaneo
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        // Simular escaneo de QR
                        scannedCode = "CARNET12345"
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.QrCodeScanner,
                    contentDescription = "Escanear QR",
                    modifier = Modifier.size(120.dp)
                )
                Text("Toca para simular escaneo de QR")
                Text("(En producción usarías ZXing o ML Kit)")
            }
        }
    }
}

@Composable
fun PagoFormScreen(carnetBeneficiario: String, navController: NavController, viewModel: CarteraViewModel) {
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val pagoState = viewModel.pagoState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Realizar Pago",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Beneficiario: $carnetBeneficiario")

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto a pagar") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val montoDouble = monto.toDoubleOrNull() ?: 0.0
                if (montoDouble > 0) {
                    viewModel.realizarPago(carnetBeneficiario, 1L, montoDouble, descripcion)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = monto.toDoubleOrNull() ?: 0.0 > 0
        ) {
            Text("Realizar Pago")
        }

        // Manejar estado del pago
        when (pagoState) {
            is PagoState.Success -> {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
            is PagoState.Error -> {
                Text(pagoState.message, color = Color.Red)
            }
            is PagoState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {}
        }
    }
}