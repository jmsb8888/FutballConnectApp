package com.task.futballconnectapp.data.bd.models

data class PostPerson(
    val id: Int,
    val name: String,
    val position: String? = null,
    val dateOfBirth: String,
    val nationality: String
)