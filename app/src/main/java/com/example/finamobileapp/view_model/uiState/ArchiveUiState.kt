package com.example.finamobileapp.view_model.uiState

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.DonutSegment
import java.time.YearMonth

@Immutable
data class ArchiveUiState(
    val selectedYearMonth: YearMonth = YearMonth.now(),
    val donutGraphExpense: List<DonutSegment> = emptyList(),

    )
