package com.example.pagos.util

import com.example.pagos.service.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Cambia esto por la URL de desarrollo / producción adecuada
    private const val BASE_URL = "http://192.168.0.100:8080/" // <- reemplaza con IP real cuando pruebes en dispositivo

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
