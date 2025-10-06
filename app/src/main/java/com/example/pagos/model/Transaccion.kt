package com.example.pagos.model

data class Transaccion(
    val idTransaccion: Long,
    val cartera: Cartera,
    val tipo: String,
    val monto: Double,
    val fecha: String,
    val descripcion: String,
    val realizadoPor: User
)