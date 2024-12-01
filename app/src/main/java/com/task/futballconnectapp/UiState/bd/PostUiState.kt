package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.Post

data class PostUiState(
    val post: Post? = null,
    val isLoading: Boolean = false
)
