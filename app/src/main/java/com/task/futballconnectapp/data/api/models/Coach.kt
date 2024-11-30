package com.task.futballconnectapp.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Coach(
    val id: Int,
    val name: String,
    val dateOfBirth: String,
    val nationality: String,
)
