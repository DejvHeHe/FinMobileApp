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
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.view.forms.DeleteForm
import com.example.finamobileapp.view_model.DashboardViewModel
import com.example.finamobileapp.view_model.interfaces.BuyIdeaActions
import com.example.finamobileapp.view_model.uiState.BuyIdeaUiState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BuyIdeaBox(
    buyIdea: BuyIdeas,
    onBuyIdeaAction: (BuyIdeaActions)->Unit,
    setBuyIdea: (BuyIdeas) -> Unit,
    buyIdeaUiState: BuyIdeaUiState
) {




    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = {/**/ },
                onLongClick = { BuyIdeaActions.ToggleOption(buyIdea.id)}
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
                    checked = buyIdeaUiState.isChecked.contains(buyIdea.id),
                    onCheckedChange = { BuyIdeaActions.ToggleIsChecked(buyIdea.id) }
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


            if (buyIdeaUiState.isChecked.contains(buyIdea.id)) {
                Column(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
                        .fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp), color = Color.Gray)

                    ExposedDropdownMenuBox(
                        expanded = buyIdeaUiState.expandedAccountType.contains(buyIdea.id),
                        onExpandedChange = { BuyIdeaActions.ToggleExpandAccountType(buyIdea.id) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = buyIdeaUiState.selectedOptionAccType[buyIdea.id]?.name ?: "Vyber účet",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Z jakého účtu?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = buyIdeaUiState.expandedAccountType.contains(buyIdea.id)) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = buyIdeaUiState.expandedAccountType.contains(buyIdea.id),
                            onDismissRequest = { BuyIdeaActions.ToggleExpandAccountType(buyIdea.id) }
                        ) {
                            TransactionAccountType.entries.forEach { accountType ->
                                DropdownMenuItem(
                                    text = { Text(accountType.name) },
                                    onClick = {
                                        BuyIdeaActions.SelectAccTypeOption(buyIdea.id,accountType)
                                        BuyIdeaActions.ToggleExpandAccountType(buyIdea.id)

                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val accountTypeEnum =
                                TransactionAccountType.entries.find { it.name == selectedOptionAccType }
                            if (accountTypeEnum != null) {
                                transactionViewModel.addTransaction(
                                    Transaction(
                                        name = buyIdea.name,
                                        amount = buyIdea.price,
                                        category = buyIdea.category,
                                        type = buyIdea.type,
                                        accountType = accountTypeEnum,
                                        date = LocalDate.now(),
                                        description = buyIdea.description,
                                        groupId = null
                                    )
                                )
                                BuyIdeaActions.DeleteBuyIdea(buyIdea)
                            }
                        },
                        enabled = !buyIdeaUiState.selectedOptionAccType.isEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Potvrdit nákup")
                    }
                }
            }
            if (buyIdeaUiState.isOpen) {
                Popup(onDismissRequest = { BuyIdeaActions.ToggleOption(buyIdea.id) }) {
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
                                        setBuyIdea(buyIdea)


                                    }
                                    .padding(12.dp)
                            )
                            Text(
                                text = "Smazat",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { BuyIdeaActions.ToggleDeleteForm(buyIdea.id) }
                                    .padding(12.dp)
                            )

                        }


                    }
                }
            }
        }
    }

    if (buyIdeaUiState.isDeleteFormOpen == buyIdea.id) {
        DeleteForm(
            onDismiss = { BuyIdeaActions.ToggleDeleteForm(buyIdea.id) },
            itemName = buyIdea.name,
            onDelete = { BuyIdeaActions.DeleteBuyIdea(buyIdea) },
            closeOptions = { BuyIdeaActions.ToggleOption(buyIdea.id) })
    }
}

