package com.example.pagos.controllers
import com.example.pagos.model.*
import com.example.pagos.service.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
class UsuarioController(private val apiService: ApiService) {
    suspend fun obtenerUsuarioActual(token: String): Result<Usuario> {
        return try {
            val response = apiService.getUsuarioActual("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarRostro(token: String, usuarioId: Long, imagen: ByteArray): Result<Boolean> {
        return try {
            val requestBody = imagen.toRequestBody("image/*".toMediaType())
            val part = MultipartBody.Part.createFormData("file", "rostro.jpg", requestBody)
            val response = apiService.registrarRostro("Bearer $token", usuarioId, part)
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}