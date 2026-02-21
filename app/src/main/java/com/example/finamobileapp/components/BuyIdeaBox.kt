package com.example.finamobileapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.models.BuyIdeas
import com.example.finamobileapp.models.Transaction
import com.example.finamobileapp.models.TransactionAccountType
import com.example.finamobileapp.models.view_model.TransactionViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BuyIdeaBox(buyIdea: BuyIdeas, transactionViewModel: TransactionViewModel) {

    var isChecked by remember { mutableStateOf(false) }
    var expandedAccountType by remember { mutableStateOf(false) }
    var selectedOptionAccType by remember { mutableStateOf("Vyber účet") }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .combinedClickable(
                onClick = {/**/},
                onLongClick = {/**/}
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
                    onCheckedChange = { isChecked = it }
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
                    Divider(modifier = Modifier.padding(bottom = 12.dp), color = Color.Gray)

                    ExposedDropdownMenuBox(
                        expanded = expandedAccountType,
                        onExpandedChange = { expandedAccountType = !expandedAccountType },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedOptionAccType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Z jakého účtu?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAccountType) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val accountTypeEnum = TransactionAccountType.entries.find { it.name == selectedOptionAccType }
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
                                transactionViewModel.deleteBuyIdea(buyIdea)
                            }
                        },
                        enabled = selectedOptionAccType != "Vyber účet",
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Text("Potvrdit nákup")
                    }
                }
            }
        }
    }
}