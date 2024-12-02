package com.task.futballconnectapp.data.bd.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Post")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val userProfileImageUrl: String,
    val mainImageUrl: String,
    val title: String,
    val description: String,
    val matchResultId: Int?,
    val personId: Int?,
    val isLiked: Boolean,
    val idBdRemote: Int? = null
)