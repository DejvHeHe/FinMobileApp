package com.example.finamobileapp.components.forms


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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.TransactionAccountType
import com.example.finamobileapp.models.TransactionCategory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyIdeaForm() {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedAccountType by remember { mutableStateOf(false) }
    var selectedOptionCategory by remember { mutableStateOf("Vyber kategorii") }
    var selectedOptionAccType by remember { mutableStateOf("Vyber z ktereho učtu posílaš peníze") }
    var description by remember { mutableStateOf("") }

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
                        if (category != TransactionCategory.SAVINGS) {
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    /*selectedOptionCategory = category.name
                                    expandedCategory = false*/
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
                    label = { Text("Účet ze kterého posíláte peníze") },
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
                                /*selectedOptionAccType = accountType.name
                                expandedAccountType = false*/
                            }
                        )
                    }
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
                    /* Logika pro uložení transakce s dnešním datem */
                    /* onDismiss() */
                },
                enabled = name.isNotBlank() &&
                        (amount.toIntOrNull() ?: 0) > 0 &&
                        selectedOptionCategory != "Vyber kategorii",
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
}