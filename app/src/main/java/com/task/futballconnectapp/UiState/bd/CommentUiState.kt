package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.Comment

data class CommentUiState(
    var commentList: List<Comment> = emptyList(),
    val isLoading: Boolean = false
)
