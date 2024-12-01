package com.task.futballconnectapp.data.bd.models

data class MatchResult(
    val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val fullTimeScoreHome: Int,
    val fullTimeScoreAway: Int
)
