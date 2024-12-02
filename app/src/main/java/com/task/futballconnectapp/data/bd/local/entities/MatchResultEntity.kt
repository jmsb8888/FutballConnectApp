package com.task.futballconnectapp.data.bd.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MatchResult")
data class MatchResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val homeTeamId: String,
    val awayTeamId: String,
    val fullTimeScoreHome: Int,
    val fullTimeScoreAway: Int,
    val idBdRemote: Int? = null
)