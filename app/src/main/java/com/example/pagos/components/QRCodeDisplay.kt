package com.example.pagos.components
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip

@Composable
fun QRCodeDisplay(
    bitmap: Bitmap?,
    isLoading: Boolean,
    onGenerateQR: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Tu Código QR",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(64.dp))
            Text("Generando QR...", modifier = Modifier.padding(top = 8.dp))
        } else if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Código QR",
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Text(
                "Muestra este código al vendedor",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "Generar QR",
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    "No hay código QR generado",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Button(onClick = onGenerateQR) {
                    Text("Generar Código QR")
                }
            }
        }
    }
}