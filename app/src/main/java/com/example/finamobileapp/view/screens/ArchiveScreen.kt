package com.example.finamobileapp.view.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamobileapp.model.entities.enums.TransactionType
import com.example.finamobileapp.view_model.ArchiveViewModel
import java.time.LocalDate


@Composable
fun ArchiveScreen() {
    val archiveViewModel: ArchiveViewModel = viewModel()
    val expenses by archiveViewModel.getSumyByCategories(
        date = LocalDate.now(),
        type = TransactionType.EXPENSE
    ).collectAsState(initial = emptyMap())

    val incomes by archiveViewModel.getSumyByCategories(
        date = LocalDate.now(),
        type = TransactionType.INCOME
    ).collectAsState(initial = emptyMap())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Vaše statistiky",

            )


    }
}