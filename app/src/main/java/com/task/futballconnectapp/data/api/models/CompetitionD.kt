package com.task.futballconnectapp.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class CompetitionD(
    val id: Int,
    val name: String,
    val emblem: String
)
