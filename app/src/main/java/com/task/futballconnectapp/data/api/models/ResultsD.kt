package com.task.futballconnectapp.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultsD(
    val homeTeam: TeamInfo,
    val awayTeam: TeamInfo,
    val fullTimeScoreHome: Int,
    val fullTimeScoreAway: Int
)
