package com.example.finamobileapp.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.Transaction


import androidx.compose.material3.AlertDialog

@Composable
fun DeleteForm(
    onDismiss: () -> Unit,
    transaction: Transaction,
    onDelete: () -> Unit,
    closeOptions: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Smazat transakci") },
        text = { Text("Opravdu chcete smazat ${transaction.name}?") },
        confirmButton = {
            TextButton(onClick = {
                println("DEBUG: Kliknuto na potvrzení!")
                onDelete()
                onDismiss()
                closeOptions()
            }) {
                Text("Ano", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Ne")
            }
        }
    )
}