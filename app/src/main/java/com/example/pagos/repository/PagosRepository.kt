package com.example.pagos.repository
import com.example.pagos.model.*
class PagosRepository(private val apiService: ApiService) {

    suspend fun login(correo: String, contrasena: String): String {
        val response = apiService.login(LoginRequest(correo, contrasena))
        return response.jwt
    }

    suspend fun getCartera(userId: Long): Cartera {
        return apiService.getCarteraByUser(userId)
    }

    suspend fun realizarRecarga(transaccion: Transaccion): Transaccion {
        return apiService.createTransaccion(transaccion)
    }

    suspend fun realizarPago(paymentRequest: PaymentRequest): Boolean {
        val response = apiService.processPayment(paymentRequest)
        return response.isSuccessful
    }

    suspend fun getHistorialTransacciones(): List<Transaccion> {
        return apiService.getTransacciones()
    }

    suspend fun getUserProfile(userId: Long): User {
        return apiService.getUserById(userId)
    }
}