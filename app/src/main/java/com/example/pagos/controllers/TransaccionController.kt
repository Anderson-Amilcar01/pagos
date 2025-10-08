package com.example.pagos.controllers

class TransaccionController(private val apiService: ApiService) {
    suspend fun obtenerTransaccionesUsuario(token: String, usuarioId: Long): Result<List<Transaccion>> {
        return try {
            val response = apiService.getTransaccionesUsuario("Bearer $token", usuarioId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener transacciones"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerPagosPendientes(token: String, usuarioId: Long): Result<List<Transaccion>> {
        return try {
            val response = apiService.getPagosPendientes("Bearer $token", usuarioId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener pagos pendientes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}