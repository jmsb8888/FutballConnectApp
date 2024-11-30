package com.task.futballconnectapp.UiState.Api

import com.task.futballconnectapp.data.api.models.Team

data class TeamsUiState(
    val teams: List<Team>? =  emptyList(),
    val isLoading: Boolean = true
)
