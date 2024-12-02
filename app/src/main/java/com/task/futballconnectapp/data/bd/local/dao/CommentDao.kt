package com.task.futballconnectapp.data.bd.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.task.futballconnectapp.data.bd.local.entities.CommentEntity

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Query("SELECT * FROM Comment WHERE idBdRemote = :postId")
    suspend fun getCommentsForPost(postId: Int): List<CommentEntity>

    @Query("SELECT * FROM Comment WHERE postId = :postId")
    suspend fun getCommentsForPostlocal(postId: Int): List<CommentEntity>

    @Delete
    suspend fun deleteComment(comment: CommentEntity)

    @Query("SELECT * FROM Comment WHERE idBdRemote IS NULL")
    suspend fun getCommentsToUpload(): List<CommentEntity>

    @Query("UPDATE Comment SET idBdRemote = :idBdRemote WHERE id = :commentId")
    suspend fun updateCommentIdBdRemote(commentId: Long, idBdRemote: Long)
}
