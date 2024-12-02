package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.Post

data class PostMatchUiState(
    var postMatches: List<Post> = emptyList(),
    val isLoading: Boolean = false
)
