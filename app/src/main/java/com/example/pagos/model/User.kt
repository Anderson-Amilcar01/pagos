package com.example.pagos.model

data class User(
    val idUsuario: Long,
    val primerNombre: String,
    val primerApellido: String,
    val carnet: String,
    val correo: String,
    val activo: Boolean
)