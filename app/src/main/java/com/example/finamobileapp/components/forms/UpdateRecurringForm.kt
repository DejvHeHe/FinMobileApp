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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRecurringForm(
    onDismiss: () -> Unit,
    transaction: Transaction,

    onRecurringUpdate: (
        groupId: String,
        name: String,
        amount: Int,
        category: TransactionCategory,
        type: TransactionType,
        description: String
    ) -> Unit,
    closeOptions: () -> Unit
) {

    var name by remember { mutableStateOf(transaction.name) }
    var amount by remember { mutableStateOf(transaction.amount.toString()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(transaction.category.name) }
    var description by remember { mutableStateOf(transaction.description) }

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
            Text(
                text = "Hromadná úprava řady",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nový název pro všechny") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nová suma pro všechny") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nová kategorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    TransactionCategory.entries.forEach { category ->
                        // U recurring plateb obvykle zakazujeme Transfer a Savings logiku
                        if (category != TransactionCategory.SAVINGS && category != TransactionCategory.TRANSFER) {
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
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Nový popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    val amountInt = amount.toIntOrNull() ?: 0
                    val selectedCategoryEnum = TransactionCategory.entries.find { it.name == selectedOption }



                    if (name.isNotBlank() && amountInt > 0 && selectedCategoryEnum != null && transaction.groupId != null) {
                        val type=selectedCategoryEnum.type
                        onRecurringUpdate(
                            transaction.groupId!!,
                            name,
                            amountInt,
                            selectedCategoryEnum,
                            type,
                            description
                        )
                        onDismiss()
                        closeOptions()
                    }
                },
                enabled = name.isNotBlank() && (amount.toIntOrNull() ?: 0) > 0,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text("Uložit změny v celé řadě")
            }
        }
    }
}