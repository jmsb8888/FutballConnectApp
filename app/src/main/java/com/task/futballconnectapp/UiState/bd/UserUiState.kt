package com.task.futballconnectapp.UiState.bd

import com.task.futballconnectapp.data.bd.models.User

data class UserUiState(
    val user: User? = null,
    val isLoading: Boolean = false
)

