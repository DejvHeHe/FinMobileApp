package com.example.finamobileapp.components.forms

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.finamobileapp.models.Transaction

@Composable
fun DeleteForm(onDismiss: () -> Unit,transaction: Transaction)
{
    Card {
        Text("Přejete si tuto transakci smazat?")
        Column {
            Text("Ano")
            Text("Ne" )
        }
    }

}