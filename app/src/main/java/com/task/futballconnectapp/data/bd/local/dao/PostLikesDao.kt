package com.task.futballconnectapp.data.bd.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.task.futballconnectapp.data.bd.local.entities.PostLikesEntity

@Dao
interface PostLikesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostLike(postLikes: PostLikesEntity): Long

    @Query("SELECT * FROM PostLikes WHERE postId = :postId AND userId = :userId")
    suspend fun getPostLike(postId: Int, userId: Int): PostLikesEntity?

    @Delete
    suspend fun deletePostLike(postLikes: PostLikesEntity)

    @Query("SELECT * FROM PostLikes WHERE idBdRemote IS NULL")
    suspend fun getPostLikesToUpload(): List<PostLikesEntity>

    @Query("UPDATE PostLikes SET idBdRemote = :idBdRemote WHERE id = :postLikeId")
    suspend fun updatePostLikeIdBdRemote(postLikeId: Long, idBdRemote: Long)

}
