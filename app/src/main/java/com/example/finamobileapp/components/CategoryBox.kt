package com.example.finamobileapp.components



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import com.example.finamobileapp.models.TransactionCategory

@Composable
fun CategoryBox(name:TransactionCategory,amount:Int)
{


    Row(modifier = Modifier
        .fillMaxWidth(0.90f)) {
        Text("$name:")
        Text("$amount")

    }
}
