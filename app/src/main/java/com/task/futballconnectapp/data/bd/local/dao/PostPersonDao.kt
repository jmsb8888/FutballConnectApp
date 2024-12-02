package com.task.futballconnectapp.data.bd.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.task.futballconnectapp.data.bd.local.entities.PostPersonEntity

@Dao
interface PostPersonDao {
    @Insert
    suspend fun insertPostPerson(person: PostPersonEntity): Long

    @Query("SELECT * FROM PostPerson")
    suspend fun getAllPostPersons(): List<PostPersonEntity>

    @Query("SELECT * FROM PostPerson WHERE id = :personId LIMIT 1")
    suspend fun getPostPersonById(personId: Int): PostPersonEntity?

    @Query("SELECT * FROM PostPerson WHERE idBdRemote IS NULL")
    suspend fun getPostPersonsToUpload(): List<PostPersonEntity>

    @Query("UPDATE PostPerson SET idBdRemote = :idBdRemote WHERE id = :postPersonId")
    suspend fun updatePostPersonIdBdRemote(postPersonId: Long, idBdRemote: Long)
}
