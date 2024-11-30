package com.task.futballconnectapp.UiState.Api

import com.task.futballconnectapp.data.api.models.CompetitionD

data class CompetitionsUiState (
    val competitions: List<CompetitionD>? =  emptyList(),
    val isLoading: Boolean = true
)