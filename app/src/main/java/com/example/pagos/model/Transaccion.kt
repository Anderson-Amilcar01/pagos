package com.example.pagos.model
import com.example.pagos.model.TipoTransaccion
data class Transaccion(
    val idTransaction: Long,
    val cartera: Cartera,
    val tipo: TipoTransaccion,
    val monto: Double,
    val fecha: String,
    val descripcion: String,
    val realizadoPor: Usuario
)
