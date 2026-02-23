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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateForm(
    onDismiss: () -> Unit,
    transaction: Transaction,
    onUpdate: (Transaction) -> Unit,

    ) {

    var name by remember { mutableStateOf(transaction.name) }
    var amount by remember { mutableStateOf(transaction.amount.toString()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(transaction.category.name) }
    var description by remember { mutableStateOf(transaction.description) }


    val startDatePickerState = rememberDatePickerState()
    var showStartDatePicker by remember { mutableStateOf(false) }

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
                onValueChange = { name = it },
                label = { Text("Jméno") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Suma") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TransactionCategory.entries.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                selectedOption = category.name
                                expanded = false
                            }
                        )
                    }
                }
            }
            if (transaction.groupId == null) {
                OutlinedButton(
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Změnit datum")
                }

            }



            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    val amountInt = amount.toIntOrNull() ?: 0
                    val selectedCategoryEnum =
                        TransactionCategory.entries.find { it.name == selectedOption }

                    val selectedDate = startDatePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: transaction.date

                    if (name.isNotBlank() && amountInt > 0 && selectedCategoryEnum != null) {

                        val updatedTransaction = transaction.copy(
                            name = name,
                            amount = amountInt,
                            category = selectedCategoryEnum,
                            type = selectedCategoryEnum.type,
                            date = selectedDate,
                            description = description
                        )

                        onUpdate(updatedTransaction)
                        onDismiss()

                    }
                },
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
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("Zrušit") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }
}