package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.Post

data class PostPlayerUiState(
    val postPlayers: List<Post> = emptyList(),
    val isLoading: Boolean = false
)
