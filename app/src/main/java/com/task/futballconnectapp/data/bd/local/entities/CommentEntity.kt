package com.task.futballconnectapp.data.bd.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Comment")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val userName: String,
    val text: String,
    val idBdRemote: Int? = null
)
