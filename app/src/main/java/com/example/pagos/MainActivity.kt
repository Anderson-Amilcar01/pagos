package com.example.pagos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.pagos.ui.PagosAppTheme
import com.example.pagos.navigation.MarketCupApp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.pagos.service.ApiService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar Retrofit - cambia por la URL real de tu API (usa http://192.168.x.x:8080 si pruebas en emulador/dispositivo)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.100:8080/") // <-- Reemplaza por la IP / URL real
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        setContent {
            PagosAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MarketCupApp(apiService = apiService)
                }
            }
        }
    }
}
