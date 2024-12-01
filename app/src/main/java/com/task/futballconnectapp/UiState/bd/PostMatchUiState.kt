package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.Post

data class PostMatchUiState(
    val postMatches: List<Post> = emptyList(),
    val isLoading: Boolean = false
)
