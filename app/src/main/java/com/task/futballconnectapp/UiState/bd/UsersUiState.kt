package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.User

data class UsersUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false
)
