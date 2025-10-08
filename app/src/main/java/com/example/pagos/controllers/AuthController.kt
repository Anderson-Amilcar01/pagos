package com.example.pagos.controllers
import com.example.pagos.model.LoginRequest
import com.example.pagos.service.ApiService

class AuthController(private val apiService: ApiService) {
    suspend fun login(correo: String, contrasena: String): Result<String> {
        return try {
            val response = apiService.login(LoginRequest(correo, contrasena))
            if (response.isSuccessful) {
                Result.success(response.body()?.jwt ?: "")
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}