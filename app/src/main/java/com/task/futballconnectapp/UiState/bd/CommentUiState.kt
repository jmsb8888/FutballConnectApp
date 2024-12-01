package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.Comment

data class CommentUiState(
    val commentList: List<Comment> = emptyList(),
    val isLoading: Boolean = false
)
