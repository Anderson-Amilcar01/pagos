package com.example.pagos.repository
import com.example.pagos.model.*
import retrofit2.http.*
import retrofit2.Response

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AuthenticationResponse

    @GET("/api/carteras/usuario/{alumnoId}")
    suspend fun getCarteraByUser(@Path("alumnoId") alumnoId: Long): Cartera

    @POST("/api/transacciones")
    suspend fun createTransaccion(@Body transaccion: Transaccion): Transaccion

    @POST("/api/qr/process-payment")
    suspend fun processPayment(@Body paymentRequest: PaymentRequest): Response<Unit>

    @GET("/api/transacciones")
    suspend fun getTransacciones(): List<Transaccion>

    @GET("/api/usuarios/{id}")
    suspend fun getUserById(@Path("id") id: Long): Usuario
}