package com.example.finamobileapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.models.Transaction

@Composable
fun TransactionBox(transaction: Transaction) {

    Card(modifier = Modifier.fillMaxWidth(0.75f).padding(10.dp)) {
        Column {
            Text("${transaction.date}", fontSize = 15.sp, textAlign = TextAlign.Right, modifier = Modifier.padding(5.dp))
            Row(modifier = Modifier.padding(5.dp)) {

                Text("${transaction.name}", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                Text("${transaction.amount}", fontSize = 20.sp,modifier = Modifier.padding(5.dp))
            }

        }




    }

}