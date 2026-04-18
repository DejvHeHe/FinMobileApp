package com.example.finamobileapp.view_model.uiState

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.DonutSegment
import java.time.YearMonth

@Immutable
data class ArchiveUiState(
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val donutGraphExpense: List<DonutSegment> = emptyList(),
    val quartalList: List<MonthlyStats> = emptyList(),
    val quartalNames: List<String> = emptyList()

)

data class MonthlyStats(
    val expenses: Int,
    val income: Int
)