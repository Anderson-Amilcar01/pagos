package com.example.pagos.model

data class Cartera(
    val idCartera: Long,
    val alumno: Usuario,
    val saldo: Double = 0.0,
    val fechaActualizacion: String
)