package com.task.futballconnectapp.data.bd.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PostPerson")
data class PostPersonEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val position: String?,
    val dateOfBirth: String,
    val nationality: String,
    val idBdRemote: Int? = null
)