package com.task.futballconnectapp.data.bd.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.task.futballconnectapp.data.bd.local.entities.MatchResultEntity

@Dao
interface MatchResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatchResult(matchResult: MatchResultEntity): Long

    @Query("SELECT * FROM MatchResult")
    suspend fun getAllMatchResults(): List<MatchResultEntity>

    @Query("SELECT * FROM MatchResult WHERE id = :matchResultId LIMIT 1")
    suspend fun getMatchResultById(matchResultId: Int): MatchResultEntity?

    @Query("SELECT * FROM MatchResult WHERE idBdRemote IS NULL")
    suspend fun getMatchResultsToUpload(): List<MatchResultEntity>

    @Query("UPDATE MatchResult SET idBdRemote = :idBdRemote WHERE id = :matchResultId")
    suspend fun updateMatchResultIdBdRemote(matchResultId: Long, idBdRemote: Long)
}

