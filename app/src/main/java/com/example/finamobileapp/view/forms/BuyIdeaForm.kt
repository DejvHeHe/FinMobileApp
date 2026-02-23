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
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyIdeaForm(onDismiss: () -> Unit, onSubmit:(BuyIdeas)->Unit, buyIdea: BuyIdeas?) {
    var name by remember { mutableStateOf(buyIdea?.name ?: "") }
    var price by remember { mutableStateOf(buyIdea?.price?.toString() ?: "") }
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedOptionCategory by remember { mutableStateOf(buyIdea?.category?.name ?: "Vyber kategorii") }
    var description by remember { mutableStateOf(buyIdea?.description ?:  "") }

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
            // Jméno
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Jméno") },
                modifier = Modifier.fillMaxWidth()
            )

            // Částka
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Suma") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Kategorie
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
                                    selectedOptionCategory = category.name
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
            }

            // Popis
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Tlačítko Potvrdit
            Button(
                onClick = {
                    val categoryEnum =
                        TransactionCategory.entries.find { it.name == selectedOptionCategory }
                    if (categoryEnum != null) {
                        onSubmit(
                            BuyIdeas(
                                name = name,
                                price = price.toIntOrNull() ?: 0,
                                category = categoryEnum,
                                type = TransactionType.EXPENSE,
                                description = description
                            )
                        )
                        onDismiss()
                    }
                },
                enabled = name.isNotBlank() &&
                        (price.toIntOrNull() ?: 0) > 0 &&
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