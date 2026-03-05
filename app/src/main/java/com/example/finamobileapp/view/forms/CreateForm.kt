package com.example.finamobileapp.view.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.view_model.CreateFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateForm(onDismiss: () -> Unit) {

    val viewModel: CreateFormViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val startDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = state.startDateMillis)
    val endDatePickerState =
        rememberDatePickerState(initialSelectedDateMillis = state.endDateMillis)

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
                label = { Text("Jmeno") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.amount,
                onValueChange = { viewModel.setAmount(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Suma") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

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
                        val isSavings = category == TransactionCategory.SAVINGS
                        val isTransferAndRecurring =
                            state.isRecurring && category == TransactionCategory.TRANSFER
                        if (!isSavings && !isTransferAndRecurring) {
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    viewModel.selectCategory(category)
                                    viewModel.toggleCategoryExpand()
                                }
                            )
                        }
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = state.isAccountTypeExpanded,
                onExpandedChange = { viewModel.toggleAccountTypeExpand() }
            ) {
                OutlinedTextField(
                    value = state.selectedAccountType?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Učet ze kterého posílate peníze") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.isAccountTypeExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = state.isAccountTypeExpanded,
                    onDismissRequest = { viewModel.toggleAccountTypeExpand() }
                ) {
                    TransactionAccountType.entries.forEach { accountType ->
                        DropdownMenuItem(
                            text = { Text(accountType.name) },
                            onClick = {
                                viewModel.selectAccountType(accountType)
                                viewModel.toggleAccountTypeExpand()
                            }
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = { viewModel.toggleStartDatePicker() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Vybrat datum zahájení")
            }

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.setDescription(it) },
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleIsRecurring() }
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = state.isRecurring,
                    onCheckedChange = null
                )
                Text(text = "Opakovaná platba", modifier = Modifier.padding(start = 8.dp))
            }

            if (state.isRecurring) {
                OutlinedButton(
                    onClick = { viewModel.toggleEndDatePicker() },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Vybrat datum konce")
                }
            }

            val isDateValid = if (state.isRecurring) {
                val start = state.startDateMillis
                val end = state.endDateMillis
                start != null && end != null && end >= start
            } else true

            Button(
                onClick = {
                    viewModel.onConfirmClicked(onSuccess = onDismiss)
                },
                enabled = state.name.isNotBlank() &&
                        (state.amount.toIntOrNull() ?: 0) > 0 &&
                        state.selectedCategory != null && isDateValid,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text("Potvrdit")
            }
        }
    }

    if (state.showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.toggleStartDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setStartDate(startDatePickerState.selectedDateMillis)
                    viewModel.toggleStartDatePicker()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.toggleStartDatePicker() }) { Text("Zrušit") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    if (state.showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { viewModel.toggleEndDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setEndDate(endDatePickerState.selectedDateMillis)
                    viewModel.toggleEndDatePicker()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.toggleEndDatePicker() }) { Text("Zrušit") }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }
}