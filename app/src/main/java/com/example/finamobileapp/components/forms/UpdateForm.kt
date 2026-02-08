package com.example.finamobileapp.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionCategory
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateForm(
    onDismiss: () -> Unit,
    transaction: Transaction,
    onUpdate: (Transaction) -> Unit,
    closeOptions: () -> Unit
) {
    // Inicializujeme stavy hodnotami z existující transakce
    var name by remember { mutableStateOf(transaction.name) }
    var amount by remember { mutableStateOf(transaction.amount.toString()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(transaction.category.name) }
    var description by remember { mutableStateOf(transaction.description) }

    // Inicializace DatePickerState s původním datem transakce (pokud je dostupné v millis)
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
                    modifier = Modifier.menuAnchor().fillMaxWidth()
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

            OutlinedButton(
                onClick = { showStartDatePicker = true },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Změnit datum")
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
                    val selectedCategoryEnum = TransactionCategory.entries.find { it.name == selectedOption }

                    val selectedDate = startDatePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: transaction.date // Pokud datum nezmění, necháme původní

                    if (name.isNotBlank() && amountInt > 0 && selectedCategoryEnum != null) {
                        // Vytvoření kopie se zachováním ID (klíčové pro Room @Update)
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
                        closeOptions()
                    }
                },
                enabled = name.isNotBlank() && (amount.toIntOrNull() ?: 0) > 0,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
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