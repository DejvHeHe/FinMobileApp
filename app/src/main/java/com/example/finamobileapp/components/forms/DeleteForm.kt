package com.example.finamobileapp.components.forms


import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
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