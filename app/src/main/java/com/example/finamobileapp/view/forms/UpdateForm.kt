package com.example.finamobileapp.view.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.view_model.UpdateFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateForm(
    onDismiss: () -> Unit,
    originalTransaction: Transaction,
    isRecurringAction: Boolean,
    viewModel: UpdateFormViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()


    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.startDateMillis
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.setName(it) },
                label = { Text("Jméno") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.setAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Suma") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Kategorie
            ExposedDropdownMenuBox(
                expanded = state.isCategoryExpanded,
                onExpandedChange = { viewModel.toggleCategoryExpand() }
            ) {
                OutlinedTextField(
                    value = state.selectedCategory?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isCategoryExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = state.isCategoryExpanded,
                    onDismissRequest = { viewModel.toggleCategoryExpand() }
                ) {
                    TransactionCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                viewModel.setCategory(category)
                                viewModel.toggleCategoryExpand()
                            }
                        )
                    }
                }
            }


            if (!state.isRecurring) {
                OutlinedButton(
                    onClick = { viewModel.toggleDatePicker() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Změnit datum")
                }
            }

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.setDescription(it) },
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    viewModel.saveUpdate(
                        original = originalTransaction,
                        isRecurringAction = isRecurringAction,
                        onSuccess = onDismiss
                    )
                },
                enabled = state.name.isNotBlank() && (state.amount.toIntOrNull() ?: 0) > 0,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Uložit změny")
            }
        }
    }

    // DatePicker Dialog
    if (state.showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.toggleDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setDate(startDatePickerState.selectedDateMillis)
                    viewModel.toggleDatePicker()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.toggleDatePicker() }) { Text("Zrušit") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }
}