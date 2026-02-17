package com.example.finamobileapp.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamobileapp.components.Graph
import com.example.finamobileapp.models.TransactionType
import com.example.finamobileapp.models.view_model.ArchiveViewModel
import java.time.LocalDate
import androidx.compose.runtime.getValue


@Composable
fun ArchiveScreen() {
    val archiveViewModel: ArchiveViewModel= viewModel()
    val graphData by archiveViewModel.getSumyByCategories(
        date = LocalDate.now(),
        type = TransactionType.EXPENSE
    ).collectAsState(initial = emptyMap())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Vaše statistiky",

        )
        Graph(graphData)




    }
}