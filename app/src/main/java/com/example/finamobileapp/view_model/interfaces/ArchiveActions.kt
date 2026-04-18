package com.example.finamobileapp.view_model.interfaces

sealed interface ArchiveActions {
    data object YearMonthPlus : ArchiveActions
    data object YearMonthMinus : ArchiveActions

}