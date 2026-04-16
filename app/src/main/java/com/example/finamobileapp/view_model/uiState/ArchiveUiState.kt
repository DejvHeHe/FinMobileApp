package com.example.finamobileapp.view_model.uiState

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.YearMonth

@Immutable
data class ArchiveUiState(
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val donutGraphExpense: Flow<Map<TransactionCategory, Int>> = emptyFlow(),

    )
