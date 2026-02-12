package com.example.finamobileapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.finamobileapp.components.forms.DeleteForm
import com.example.finamobileapp.components.forms.UpdateForm
import com.example.finamobileapp.models.Transaction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionBox(
    transaction: Transaction,
    onDelete: () -> Unit,
    onUpdate: (Transaction) -> Unit
) {
    var isOptionsOpen by remember { mutableStateOf(false) }
    var isOpen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(10.dp)
            .combinedClickable(
                onClick = { isOpen=!isOpen},
                onLongClick = { isOptionsOpen = true }
            )
    ) {
        Column {
            Text(
                text = "${transaction.date}",
                fontSize = 15.sp,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            Row(modifier = Modifier.padding(5.dp)) {
                Text(
                    text = transaction.name,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "${transaction.amount}",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp)
                )

            }
        }

        if (isOptionsOpen) {
            ShowOptions(
                transaction = transaction,
                closeOptions = { isOptionsOpen = false },
                onDelete = onDelete,
                onUpdate = onUpdate
            )
        }
        if(isOpen)
        {
            Column(modifier = Modifier.padding(20.dp)) {
                HorizontalDivider(Modifier.fillMaxWidth())
                Text(transaction.description) }


        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowOptions(
    closeOptions: () -> Unit,
    transaction: Transaction,
    onDelete: () -> Unit,
    onUpdate: (Transaction) -> Unit
) {
    var isDeleteFormOpen by remember { mutableStateOf(false) }
    var isUpdateFormOpen by remember { mutableStateOf(false) }


    Popup(onDismissRequest = closeOptions) {
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
                    text = "Smazat",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isDeleteFormOpen = true }
                        .padding(12.dp)
                )
                Text(
                    text = "Upravit",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isUpdateFormOpen = true }
                        .padding(12.dp)
                )
                if(transaction.groupId!=null)
                {
                    Text(
                        text = "Smazat",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isDeleteFormOpen = true }
                            .padding(12.dp)
                    )
                    Text(
                        text = "Upravit",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isUpdateFormOpen = true }
                            .padding(12.dp)
                    )
                }
            }
        }
    }


    if (isDeleteFormOpen) {
        DeleteForm(
            onDismiss = { isDeleteFormOpen = false },
            transaction = transaction,
            onDelete = onDelete,
            closeOptions = closeOptions
        )
    }


    if (isUpdateFormOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                isUpdateFormOpen = false
                closeOptions()
            },
            containerColor = Color(0xFFF0F0F0)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 400.dp)
                    .padding(16.dp)
            ) {
                UpdateForm(
                    onDismiss = { isUpdateFormOpen = false },
                    transaction = transaction,
                    onUpdate = onUpdate,
                    closeOptions = closeOptions
                )
            }
        }
    }
}