package com.example.finamobileapp.view_model.interfaces

sealed interface DonutGraphActions {
    data object YearMonthPlus : DonutGraphActions
    data object YearMonthMinus : DonutGraphActions
    data object LoadExpenses : DonutGraphActions
}