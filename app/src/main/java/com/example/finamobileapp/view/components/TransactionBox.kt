package com.example.finamobileapp.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.model.entities.Transaction

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionBox(
    transaction: Transaction,
    isExpanded: Boolean,
    onExpandClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(10.dp)
            .combinedClickable(
                onClick = onExpandClick,
                onLongClick = onLongClick
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
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "${transaction.amount} Kč",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

        if (isExpanded) {
            Column(modifier = Modifier.padding(20.dp)) {
                HorizontalDivider(Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = transaction.description.ifEmpty { "Bez popisu" })
            }
        }
    }
}