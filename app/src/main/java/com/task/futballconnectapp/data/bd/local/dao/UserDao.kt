package com.task.futballconnectapp.data.bd.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.task.futballconnectapp.data.bd.local.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM Users")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM Users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM Users WHERE idBdRemote IS NULL")
    suspend fun getUsersToUpload(): List<UserEntity>

    @Query("UPDATE Users SET idBdRemote = :idBdRemote WHERE id = :userId")
    suspend fun updateUserIdBdRemote(userId: Long, idBdRemote: Long)
}