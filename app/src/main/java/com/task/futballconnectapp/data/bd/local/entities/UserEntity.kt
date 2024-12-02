package com.task.futballconnectapp.data.bd.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val email: String,
    val password: String,
    val imagePerfil: String? = null,
    val idBdRemote: Int? = null
)
