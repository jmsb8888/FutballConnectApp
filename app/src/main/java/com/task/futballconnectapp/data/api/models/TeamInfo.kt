package com.task.futballconnectapp.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class TeamInfo(
    val id: Int,
    val shortName: String,
    val crest: String
)
