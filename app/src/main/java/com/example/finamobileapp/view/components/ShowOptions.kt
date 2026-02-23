package com.example.finamobileapp.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.finamobileapp.model.entities.Transaction

@Composable
fun ShowOptions(
    transaction: Transaction,
    closeOptions: () -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    onRecurringDelete: () -> Unit,
    onRecurringUpdate: () -> Unit,

    ) {
    Popup(onDismissRequest = closeOptions) {
        Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(0.55f),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column {
                OptionItem(text = "Smazat transakci", onClick = onDelete)
                OptionItem(text = "Upravit transakci", onClick = onUpdate)

                if (transaction.groupId != null) {
                    HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                    OptionItem(text = "Smazat celou řadu", onClick = onRecurringDelete)
                    OptionItem(text = "Upravit celou řadu", onClick = onRecurringUpdate)
                }
            }
        }
    }
}

@Composable
private fun OptionItem(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
    )
}