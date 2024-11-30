package com.task.futballconnectapp.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val id: Int,
    val name: String,
    val shortName: String,
    val crest: String,
    val venue: String,
    val clubColors: String,
    val coach: Coach,
    val squad: List<Player>,
)
