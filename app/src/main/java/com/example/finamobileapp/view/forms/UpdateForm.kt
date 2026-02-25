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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.model.entities.enums.TransactionCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateForm(
    name: String,
    amount: String,
    description: String,
    selectedOption: String,
    expandedCategory: Boolean,
    showStartDatePicker: Boolean,
    selectedDateMillis: Long?,
    isRecurring: Boolean, // Přidáno pro logiku zobrazení tlačítka data

    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCategoryExpand: () -> Unit,
    onCategorySelect: (String) -> Unit,
    onDatePickerToggle: () -> Unit,
    onDateChange: (Long?) -> Unit,
    onSave: () -> Unit,

    ) {
    // Inicializujeme stav pickeru podle dat z ViewModelu
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
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
                value = name,
                onValueChange = onNameChange,
                label = { Text("Jméno") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Suma") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ExposedDropdownMenuBox(
                expanded = expandedCategory,
                onExpandedChange = { onCategoryExpand() }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = onCategoryExpand
                ) {
                    TransactionCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = { onCategorySelect(category.name) }
                        )
                    }
                }
            }

            // Pokud to není opakovaná platba (groupId == null), umožníme změnu data
            if (!isRecurring) {
                OutlinedButton(
                    onClick = onDatePickerToggle,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Změnit datum")
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = onSave,

                enabled = name.isNotBlank() && (amount.toIntOrNull() ?: 0) > 0,
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

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDatePickerToggle,
            confirmButton = {
                TextButton(onClick = {
                    onDateChange(startDatePickerState.selectedDateMillis)
                    onDatePickerToggle()
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = onDatePickerToggle) { Text("Zrušit") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }
}