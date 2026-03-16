package com.example.finamobileapp.view_model.uiState

data class GoalUiState(
    val isEditGoalOpen: Boolean = false,
    val investmentGoal: String? = null,
    val savingsGoal: String? = null,
    val isLoading: Boolean=false

)