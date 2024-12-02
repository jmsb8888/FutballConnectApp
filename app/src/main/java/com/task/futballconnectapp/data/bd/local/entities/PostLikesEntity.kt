package com.task.futballconnectapp.data.bd.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PostLikes")
data class PostLikesEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val userId: Int,
    val idBdRemote: Int? = null,

    )

