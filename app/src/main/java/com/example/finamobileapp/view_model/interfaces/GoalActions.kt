package com.example.finamobileapp.view_model.interfaces

import com.example.finamobileapp.model.entities.MonthlyGoal

interface GoalActions {
    data class ToggleEditGoal(val isOpen: Boolean): GoalActions
    data class SetInvestmentGoal(val investment:String): GoalActions
    data class  SetSavingsGoal(val savings:String): GoalActions
    object SetGoal: GoalActions
}