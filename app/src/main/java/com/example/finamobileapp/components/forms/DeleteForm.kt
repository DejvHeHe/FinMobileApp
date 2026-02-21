package com.example.finamobileapp.components.forms


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteForm(
    onDismiss: () -> Unit,
    itemName: String,
    onDelete: () -> Unit,
    closeOptions: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Smazat položku") },
        text = { Text("Opravdu chcete smazat ${itemName}?") },
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