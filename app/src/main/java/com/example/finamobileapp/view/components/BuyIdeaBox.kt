package com.example.finamobileapp.view.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.view.forms.DeleteForm
import com.example.finamobileapp.view_model.interfaces.BuyIdeaActions
import com.example.finamobileapp.view_model.uiState.BuyIdeaUiState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BuyIdeaBox(
    buyIdea: BuyIdeas,
    onBuyIdeaAction: (BuyIdeaActions) -> Unit,
    buyIdeaUiState: BuyIdeaUiState
) {

    val isChecked = buyIdeaUiState.isChecked.contains(buyIdea.id)
    val isOptionOpen = buyIdeaUiState.isOptionOpen == buyIdea.id
    val isDeleteFormOpen = buyIdeaUiState.isDeleteFormOpen == buyIdea.id
    val isExpanded = buyIdeaUiState.expandedAccountType.contains(buyIdea.id)
    val selectedAccount = buyIdeaUiState.selectedOptionAccType[buyIdea.id]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = { /* Případně detail */ },
                onLongClick = { onBuyIdeaAction(BuyIdeaActions.ToggleOption(buyIdea.id)) }
            ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9)),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onBuyIdeaAction(BuyIdeaActions.ToggleIsChecked(buyIdea.id)) }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = buyIdea.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = buyIdea.category.name,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                    if (buyIdea.description.isNotBlank()) {
                        Text(
                            text = buyIdea.description,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = "${buyIdea.price} Kč",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color(0xFF2E7D32)
                )
            }


            if (isChecked) {
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 12.dp),
                        color = Color.Gray
                    )

                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = {
                            onBuyIdeaAction(
                                BuyIdeaActions.ToggleExpandAccountType(
                                    buyIdea.id
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedAccount?.name ?: "Vyber účet",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Z jakého účtu?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = {
                                onBuyIdeaAction(
                                    BuyIdeaActions.ToggleExpandAccountType(
                                        buyIdea.id
                                    )
                                )
                            }
                        ) {
                            TransactionAccountType.entries.forEach { accountType ->
                                DropdownMenuItem(
                                    text = { Text(accountType.name) },
                                    onClick = {
                                        onBuyIdeaAction(
                                            BuyIdeaActions.SelectAccTypeOption(
                                                buyIdea.id,
                                                accountType
                                            )
                                        )
                                        onBuyIdeaAction(
                                            BuyIdeaActions.ToggleExpandAccountType(
                                                buyIdea.id
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onBuyIdeaAction(BuyIdeaActions.AddTransaction(buyIdea)) },

                        enabled = selectedAccount != null,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Potvrdit nákup")
                    }
                }
            }


            if (isOptionOpen) {
                Popup(onDismissRequest = { onBuyIdeaAction(BuyIdeaActions.ToggleOption(buyIdea.id)) }) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(0.45f),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column {
                            Text(
                                text = "Upravit",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onBuyIdeaAction(BuyIdeaActions.PrepareUpdate(buyIdea))
                                        onBuyIdeaAction(BuyIdeaActions.ToggleOption(buyIdea.id))
                                    }
                                    .padding(12.dp)
                            )
                            Text(
                                text = "Smazat",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onBuyIdeaAction(
                                            BuyIdeaActions.ToggleDeleteForm(
                                                buyIdea.id
                                            )
                                        )
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }


    if (isDeleteFormOpen) {
        DeleteForm(
            onDismiss = { onBuyIdeaAction(BuyIdeaActions.ToggleDeleteForm(null)) },
            itemName = buyIdea.name,
            onDelete = {
                onBuyIdeaAction(BuyIdeaActions.DeleteBuyIdea(buyIdea))
                onBuyIdeaAction(BuyIdeaActions.ToggleDeleteForm(null))
            },
            closeOptions = { onBuyIdeaAction(BuyIdeaActions.ToggleOption(buyIdea.id)) }
        )
    }
}