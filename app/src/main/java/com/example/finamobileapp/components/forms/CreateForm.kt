package com.example.finamobileapp.components.forms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.TransactionCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateForm(onDismiss: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Vyber kategorii") }
    var description by remember { mutableStateOf("") }
    var isRecurring by remember { mutableStateOf(false) }

    // Start Date stavy
    val startDatePickerState = rememberDatePickerState()
    var showStartDatePicker by remember { mutableStateOf(false) }

    // End Date stavy
    val endDatePickerState = rememberDatePickerState()
    var showEndDatePicker by remember { mutableStateOf(false) }

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
                label = { Text("Jmeno") },
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
                Text("Vybrat datum zahájení")
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isRecurring = !isRecurring }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = isRecurring,
                    onClick = { isRecurring = !isRecurring }
                )
                Text(text = "Recurring", modifier = Modifier.padding(start = 8.dp))
            }

            if (isRecurring) {
                OutlinedButton(
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text("Vybrat datum konce")
                }
            }

            Button(
                onClick = { onDismiss() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text("Potvrdit")
            }
        }
    }

    // Dialog pro Start Date
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

    // Dialog pro End Date
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("Zrušit") }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }
}