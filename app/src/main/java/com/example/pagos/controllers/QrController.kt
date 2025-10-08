package com.example.pagos.controllers
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.pagos.model.PaymentRequest
import com.example.pagos.service.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
class QrController(private val apiService: ApiService) {
    suspend fun generarCodigoQr(token: String, usuarioId: Long): Result<Bitmap> {
        return try {
            val response = apiService.generarQrCode("Bearer $token", usuarioId)
            if (response.isSuccessful) {
                val bytes = response.body()?.bytes()
                if (bytes != null) {
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    Result.success(bitmap)
                } else {
                    Result.failure(Exception("Error al decodificar QR"))
                }
            } else {
                Result.failure(Exception("Error al generar QR"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun solicitarPago(token: String, request: PaymentRequest): Result<Long> {
        return try {
            val response = apiService.solicitarPago("Bearer $token", request)
            if (response.isSuccessful) {
                val transactionId = response.body()?.get("transactionId")?.asLong
                Result.success(transactionId ?: -1)
            } else {
                Result.failure(Exception("Error al solicitar pago"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmarPago(token: String, transactionId: Long, imagen: ByteArray): Result<Boolean> {
        return try {
            val requestBody = imagen.toRequestBody("image/*".toMediaType())
            val part = MultipartBody.Part.createFormData("file", "confirmacion.jpg", requestBody)
            val response = apiService.confirmarPago("Bearer $token", transactionId, part)
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
