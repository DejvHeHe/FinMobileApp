package com.example.finamobileapp

import androidx.lifecycle.viewmodel.compose.viewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.components.BalanceBox
import com.example.finamobileapp.components.Footer
import com.example.finamobileapp.components.TypeBox
import com.example.finamobileapp.components.forms.CreateForm
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TypeBoxData
import com.example.finamobileapp.models.view_model.TransactionViewModel
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dashboard()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard() {
    val transactionViewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    var showSheet by remember { mutableStateOf(false) }


    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )


    Scaffold(
        bottomBar = {
            Footer(onAddClick = { showSheet = true })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFA69D9D)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("Fin App", fontSize = 40.sp, modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val incomeData = TypeBoxData(
                    title = "Income",
                    items = mapOf(TransactionCategory.SALARY to 45000),
                    totalSum = 45000
                )
                TypeBox(incomeData, modifier = Modifier.weight(1f))

                val expenseData = TypeBoxData(
                    title = "Expenses",
                    items = mapOf(
                        TransactionCategory.FUN to 45000,
                        TransactionCategory.FOOD to 2000
                    ),
                    totalSum = 47150
                )
                TypeBox(expenseData, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))
            Text("Váš zustatek", fontSize = 28.sp)
            BalanceBox(-5000)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }


    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFFD9D9D9),

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 300.dp)
            ) {
                CreateForm(onDismiss = { showSheet = false }, viewModel = transactionViewModel)
            }
        }
    }
}