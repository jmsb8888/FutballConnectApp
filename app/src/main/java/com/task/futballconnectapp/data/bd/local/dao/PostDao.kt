package com.task.futballconnectapp.data.bd.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.task.futballconnectapp.data.bd.local.entities.PostEntity

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity): Long

    @Query("SELECT * FROM Post WHERE id = :id")
    suspend fun getPostById(id: Int): PostEntity?

    @Query("SELECT * FROM Post")
    suspend fun getAllPosts(): List<PostEntity>

    @Delete
    suspend fun deletePost(post: PostEntity)

    @Query("SELECT * FROM Post WHERE idBdRemote IS NULL")
    suspend fun getPostsToUpload(): List<PostEntity>

    @Query("UPDATE Post SET idBdRemote = :idBdRemote WHERE id = :postId")
    suspend fun updatePostIdBdRemote(postId: Int, idBdRemote: Int)
}