package com.example.pagos.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pagos.viewmodel.CarteraViewModel
import com.example.pagos.viewmodel.RecargaState

@Composable
fun RecargaScreen(navController: NavController) {
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val viewModel: CarteraViewModel = viewModel()
    val recargaState = viewModel.recargaState.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Recargar Saldo",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = monto,
            onValueChange = { monto = it },
            label = { Text("Monto a recargar") },
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
                    viewModel.realizarRecarga(montoDouble, descripcion, 1L) // userId debería venir del login
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = monto.toDoubleOrNull() ?: 0.0 > 0
        ) {
            Text("Realizar Recarga")
        }

        // Manejar estado de la recarga
        when (recargaState) {
            is RecargaState.Success -> {
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
            is RecargaState.Error -> {
                Text(recargaState.message, color = Color.Red)
            }
            is RecargaState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {}
        }
    }
}