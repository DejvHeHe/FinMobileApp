package com.example.finamobileapp.components.forms

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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionAccountType
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.view_model.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateForm(onDismiss: () -> Unit, viewModel: TransactionViewModel) {

    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedAccountType by remember { mutableStateOf(false) }
    var selectedOptionCategory by remember { mutableStateOf("Vyber kategorii") }
    var selectedOptionAccType by remember { mutableStateOf("Vyber z ktereho učtu posílaš peníze") }
    var description by remember { mutableStateOf("") }
    var isRecurring by remember { mutableStateOf(false) }


    val startDatePickerState = rememberDatePickerState()
    var showStartDatePicker by remember { mutableStateOf(false) }


    val endDatePickerState = rememberDatePickerState()
    var showEndDatePicker by remember { mutableStateOf(false) }
    var groupId: String? = null

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
                expanded = expandedCategory,
                onExpandedChange = { expandedCategory = !expandedCategory }
            ) {
                OutlinedTextField(
                    value = selectedOptionCategory,
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
                    onDismissRequest = { expandedCategory = false }
                ) {
                    TransactionCategory.entries.forEach { category ->

                        val isSavings = category == TransactionCategory.SAVINGS
                        val isTransferAndRecurring =
                            isRecurring && category == TransactionCategory.TRANSFER
                        if (!isSavings && !isTransferAndRecurring) {
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedOptionCategory = category.name
                                    expandedCategory = false
                                }
                            )

                        }

                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedAccountType,
                onExpandedChange = { expandedAccountType = !expandedAccountType }
            ) {
                OutlinedTextField(
                    value = selectedOptionAccType,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Učet ze kterého posílate peníze") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAccountType) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedAccountType,
                    onDismissRequest = { expandedAccountType = false }
                ) {
                    TransactionAccountType.entries.forEach { accountType ->
                        DropdownMenuItem(
                            text = { Text(accountType.name) },
                            onClick = {
                                selectedOptionAccType = accountType.name
                                expandedAccountType = false
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
            val isDateValid = if (isRecurring) {
                val start = startDatePickerState.selectedDateMillis
                val end = endDatePickerState.selectedDateMillis
                start != null && end != null && end >= start
            } else true



            Button(
                onClick = {

                    val amountInt = amount.toIntOrNull() ?: 0
                    val selectedCategoryEnum =
                        TransactionCategory.entries.find { it.name == selectedOptionCategory }
                    val selectedAccountTypeEnum =
                        TransactionAccountType.entries.find { it.name == selectedOptionAccType }
                    val selectedDate = startDatePickerState.selectedDateMillis?.let { millis ->
                        java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: java.time.LocalDate.now()

                    val endDate = endDatePickerState.selectedDateMillis?.let { millis ->
                        java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                    } ?: java.time.LocalDate.now()


                    if (name.isNotBlank() && amountInt > 0 && selectedCategoryEnum != null && selectedAccountTypeEnum != null && !endDate.isBefore(
                            selectedDate
                        )
                    ) {
                        if (isRecurring) {
                            groupId = java.util.UUID.randomUUID().toString()
                        }
                        val newTransaction = Transaction(
                            name = name,
                            amount = amountInt,
                            type = selectedCategoryEnum.type,
                            category = selectedCategoryEnum,
                            accountType = selectedAccountTypeEnum,
                            date = selectedDate,
                            description = description,
                            groupId = groupId
                        )
                        if (isRecurring) {

                            viewModel.addRecurring(newTransaction, endDate)


                        } else {
                            viewModel.addTransaction(newTransaction)

                        }



                        onDismiss()
                    }
                },

                enabled = name.isNotBlank() &&
                        (amount.toIntOrNull() ?: 0) > 0 &&
                        selectedOptionCategory != "Vyber kategorii" && isDateValid,

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

