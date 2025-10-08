package com.example.pagos.service
import com.example.pagos.model.*
import com.example.pagos.model.dasboardData.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthenticationResponse>

    @GET("/api/usuarios/{id}")
    suspend fun getUsuarioActual(@Header("Authorization") token: String): Response<Usuario>

    @GET("/api/transacciones/usuario/{usuarioId}")
    suspend fun getTransaccionesUsuario(
        @Header("Authorization") token: String,
        @Path("usuarioId") usuarioId: Long
    ): Response<List<Transaccion>>

    @GET("/api/transacciones/pendientes/{usuarioId}")
    suspend fun getPagosPendientes(
        @Header("Authorization") token: String,
        @Path("usuarioId") usuarioId: Long
    ): Response<List<Transaccion>>

    @GET("/api/qr/generate/{userId}")
    suspend fun generarQrCode(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long
    ): Response<ResponseBody>

    @POST("/api/qr/request-payment")
    suspend fun solicitarPago(
        @Header("Authorization") token: String,
        @Body paymentRequest: PaymentRequest
    ): Response<JsonObject>

    @POST("/api/qr/confirm-payment/{transactionId}")
    @Multipart
    suspend fun confirmarPago(
        @Header("Authorization") token: String,
        @Path("transactionId") transactionId: Long,
        @Part file: MultipartBody.Part
    ): Response<JsonObject>

    @POST("/api/usuarios/{id}/registrar-rostro")
    @Multipart
    suspend fun registrarRostro(
        @Header("Authorization") token: String,
        @Path("id") usuarioId: Long,
        @Part file: MultipartBody.Part
    ): Response<JsonObject>

    @GET("/api/dashboard/estadisticas-generales")
    suspend fun getEstadisticasGenerales(@Header("Authorization") token: String): Response<EstadisticasGenerales>

    @GET("/api/dashboard/flujo-ultimos-7-dias")
    suspend fun getFlujoUltimos7Dias(@Header("Authorization") token: String): Response<List<FlujoDiario>>

    @GET("/api/dashboard/registros-por-mes")
    suspend fun getRegistrosPorMes(@Header("Authorization") token: String): Response<List<RegistrosMensuales>>
}
