package com.task.futballconnectapp.UiState.Api

import com.task.futballconnectapp.data.api.models.ResultsD

data class ResultsUiState(
    val results: List<ResultsD>? =  emptyList(),
    val isLoading: Boolean = true
)
