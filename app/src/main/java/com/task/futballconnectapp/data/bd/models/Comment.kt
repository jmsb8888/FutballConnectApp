package com.task.futballconnectapp.data.bd.models

data class Comment(
    val id: Int?,
    val postId: Int,
    val userName: String,
    val text: String
)