package com.example.pagos.model
data class PaymentRequest(
    val carnetBeneficiario: String,
    val idPagador: Long,
    val monto: Double,
    val descripcion: String
)