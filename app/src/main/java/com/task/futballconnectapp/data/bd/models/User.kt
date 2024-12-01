package com.task.futballconnectapp.data.bd.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val imagePerfil: String?
)