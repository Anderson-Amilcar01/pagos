package com.example.pagos.controllers
import com.example.pagos.model.dasboardData.*
import com.example.pagos.service.ApiService

class DashboardController(private val apiService: ApiService) {
    suspend fun obtenerEstadisticasGenerales(token: String): Result<EstadisticasGenerales> {
        return try {
            val response = apiService.getEstadisticasGenerales("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener estadísticas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerFlujoUltimos7Dias(token: String): Result<List<FlujoDiario>> {
        return try {
            val response = apiService.getFlujoUltimos7Dias("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener flujo"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerRegistrosMensuales(token: String): Result<List<RegistrosMensuales>> {
        return try {
            val response = apiService.getRegistrosPorMes("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error al obtener registros mensuales"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}