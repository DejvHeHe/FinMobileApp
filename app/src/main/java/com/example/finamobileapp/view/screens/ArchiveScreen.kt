package com.example.finamobileapp.view.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamobileapp.view.components.BarGraph
import com.example.finamobileapp.view.components.DonutGraph
import com.example.finamobileapp.view_model.ArchiveViewModel
import com.example.finamobileapp.view_model.interfaces.ArchiveActions

@Preview
@Composable
fun ArchiveScreen() {
    val archiveViewModel: ArchiveViewModel = viewModel()
    val uiState = archiveViewModel.uiState.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp) // Toto udělá odsazení mezi Row a Textem
    ) {
        // 1. Horní řada s výběrem měsíce
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { archiveViewModel.onActions(ArchiveActions.YearMonthMinus) }) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Předchozí měsíc")
            }

            Text(
                text = uiState.value.selectedYearMonth.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            IconButton(onClick = { archiveViewModel.onActions(ArchiveActions.YearMonthPlus) }) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Následující měsíc")
            }
        }
        BarGraph(uiState.value.quartalList, uiState.value.quartalNames)
        if (uiState.value.donutGraphExpense.isNotEmpty()) {
            DonutGraph(
                uiState.value.donutGraphExpense, modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            )
            uiState.value.donutGraphExpense.forEach { it ->
                Row() {
                    Column() {
                        Text(it.category.toString())
                    }
                    Column() {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = it.color,
                                    shape = CircleShape
                                )
                        )
                    }
                }

            }
        }


    }


}