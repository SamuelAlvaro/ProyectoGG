package com.example.guccigame

data class UsuariosSqlite(
    val correo: String,
    val password: String,
    val usuario: String,
    val puntuacion: Int = 0
)
