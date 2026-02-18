package com.example.finamobileapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.finamobileapp.models.TransactionType
import com.example.finamobileapp.models.view_model.ArchiveViewModel
import java.time.LocalDate


@Composable
fun DualGraph(archiveViewModel: ArchiveViewModel) {
    val graphDataExpense by archiveViewModel.getSumyByCategories(
        date = LocalDate.now(),
        type = TransactionType.EXPENSE
    ).collectAsState(initial = emptyMap())

    val graphDataIncome by archiveViewModel.getSumyByCategories(
        date = LocalDate.now(),
        type = TransactionType.INCOME
    ).collectAsState(initial = emptyMap())
    Row {
        Graph(graphDataIncome)
        Graph(graphDataExpense)
    }
}