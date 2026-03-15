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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.view_model.interfaces.BuyIdeaActions
import com.example.finamobileapp.view_model.uiState.BuyIdeaUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuyIdeaForm(
    onBuyIdeaActions: (BuyIdeaActions) -> Unit,
    buyIdeaUiState: BuyIdeaUiState
) {


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
                value = buyIdeaUiState.formName,
                onValueChange = { onBuyIdeaActions(BuyIdeaActions.SetFormName(it)) },
                label = { Text("Jméno") },
                modifier = Modifier.fillMaxWidth()
            )

            // Částka
            OutlinedTextField(
                value = buyIdeaUiState.formPrice,
                onValueChange = { onBuyIdeaActions(BuyIdeaActions.SetFormPrice(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Suma") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // Kategorie
            ExposedDropdownMenuBox(
                expanded = buyIdeaUiState.formExpandedCategory,
                onExpandedChange = { onBuyIdeaActions(BuyIdeaActions.SetFormExpandedCategory(!buyIdeaUiState.formExpandedCategory)) }
            ) {
                OutlinedTextField(
                    value = buyIdeaUiState.formCategory?.name ?: "Vyber kategorii",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategorie") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = buyIdeaUiState.formExpandedCategory) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = buyIdeaUiState.formExpandedCategory,
                    onDismissRequest = {
                        onBuyIdeaActions(
                            BuyIdeaActions.SetFormExpandedCategory(
                                false
                            )
                        )
                    }
                ) {
                    TransactionCategory.entries.forEach { category ->
                        if (category != TransactionCategory.SAVINGS) {
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    onBuyIdeaActions(BuyIdeaActions.SetFormCategory(category))
                                    onBuyIdeaActions(BuyIdeaActions.SetFormExpandedCategory(false))
                                }
                            )
                        }
                    }
                }
            }

            // Popis
            OutlinedTextField(
                value = buyIdeaUiState.formDescription,
                onValueChange = { onBuyIdeaActions(BuyIdeaActions.SetFormDescription(it)) },
                label = { Text("Popis") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            // Tlačítko Potvrdit
            Button(
                onClick = {
                    onBuyIdeaActions(BuyIdeaActions.OnBuyIdeaSubmit)
                    onBuyIdeaActions(BuyIdeaActions.SetBuyIdeaSheet(false))
                },
                enabled = buyIdeaUiState.formName.isNotBlank() &&
                        (buyIdeaUiState.formPrice.toIntOrNull() ?: 0) > 0 &&
                        buyIdeaUiState.formCategory != null,
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