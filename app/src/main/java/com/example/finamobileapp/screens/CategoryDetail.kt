package com.example.finamobileapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.finamobileapp.components.TransactionBox
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.view_model.TransactionViewModel
import java.time.LocalDate



@Composable
fun CategoryDetail(categoryName:String) {
    val transactionViewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val transactions=transactionViewModel.getTransactions(LocalDate.now(), TransactionCategory.valueOf(categoryName))
        .collectAsState(initial = emptyList())
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFA69D9D)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$categoryName",
                modifier = Modifier.padding(30.dp), fontSize = 40.sp
            )

            transactions.value.forEach { item ->
                TransactionBox(
                    transaction = item,
                    onDelete = {
                        transactionViewModel.deleteTransaction(item)
                    },
                    onUpdate = { updatedItem ->
                        transactionViewModel.updateTransaction(updatedItem)
                    },
                    onRecurringDelete = {
                        if (item.groupId != null) {

                            transactionViewModel.deleteRecurring(item.groupId, today = LocalDate.now())
                        }
                    } ,
                    onRecurringUpdate = { groupId, newName, newAmount, newCategory,newType, newDescription ->

                        transactionViewModel.updateRecurring(
                            groupId = groupId,
                            name = newName,
                            amount = newAmount,
                            category = newCategory,
                            type=newType,
                            description = newDescription
                        )
                    }
                )
            }




        }
    }
}