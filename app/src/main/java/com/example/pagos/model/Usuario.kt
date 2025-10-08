package com.example.pagos.model
data class Usuario(
    val idUsuario: Long,
    val primerNombre: String,
    val primerApellido: String,
    val carnet: String,
    val correo: String,
    val activo: Boolean = true,
    val rol: Rol,
    val grado: Grado? = null,
    val faceId: String? = null
)