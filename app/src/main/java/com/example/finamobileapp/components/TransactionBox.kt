package com.example.finamobileapp.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.models.Transaction
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Popup
import com.example.finamobileapp.components.forms.DeleteForm

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionBox(transaction: Transaction) {
    var isOpen by remember { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth(0.75f)
        .padding(10.dp)
        .combinedClickable(
            onClick = { /* TODO */ },
            onLongClick = { isOpen=true }
        )
    ) {

        Column {
            Text("${transaction.date}", fontSize = 15.sp, textAlign = TextAlign.Right, modifier = Modifier.fillMaxWidth().padding(5.dp))
            Row(modifier = Modifier.padding(5.dp)) {

                Text("${transaction.name}", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
                Text("${transaction.amount}", fontSize = 20.sp, modifier = Modifier.padding(5.dp))
            }
        }
        if(isOpen)
        {
            ShowOptions(closeOptions={
                isOpen=false
            },transaction)
        }


    }



}
@Composable
fun ShowOptions(closeOptions:()->Unit,transaction: Transaction)
{
    var isDeleteFormOpen by remember { mutableStateOf(false) }
    Popup(onDismissRequest = closeOptions) {

        Card(modifier = Modifier.padding(10.dp).fillMaxWidth(0.40f), colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black)) {
            Column {
                Text("Smazat",modifier = Modifier.padding(vertical = 8.dp).clickable { isDeleteFormOpen=true })
                Text("Upravit",modifier = Modifier.padding(vertical = 8.dp))

            }


        }

    }
    if(isDeleteFormOpen)
    {
        DeleteForm(onDismiss ={isDeleteFormOpen=false},transaction)
    }


}