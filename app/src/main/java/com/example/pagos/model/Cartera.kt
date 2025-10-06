package com.example.pagos.model

data class Cartera(
    val idCartera: Long,
    val alumno: User,
    val saldo: Double,
    val fechaActualizacion: String
)