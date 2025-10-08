package com.example.pagos.model.dasboardData

data class EstadisticasGenerales(
    val totalUsuariosActivos: Long,
    val ingresosTotales: Double,
    val transaccionesHoy: Long,
    val nuevosUsuariosMes: Long
)
