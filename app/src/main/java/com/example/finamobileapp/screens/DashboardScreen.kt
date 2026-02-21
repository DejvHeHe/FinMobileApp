package com.example.finamobileapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finamobileapp.components.BalanceBox
import com.example.finamobileapp.components.BuyIdeasDashboard
import com.example.finamobileapp.components.GoalBox
import com.example.finamobileapp.components.TypeBox
import com.example.finamobileapp.components.forms.BuyIdeaForm
import com.example.finamobileapp.models.TransactionAccountType
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.view_model.MonthlyGoalViewModel
import com.example.finamobileapp.models.view_model.TransactionViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavHostController, transactionViewModel: TransactionViewModel) {
    val monthGoalViewModel: MonthlyGoalViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val scrollState = rememberScrollState()

    val currentBalanceRegular by transactionViewModel
        .getBalance(LocalDate.now(), TransactionAccountType.REGULAR)
        .collectAsState(initial = 0)

    val currentBalanceSavings by transactionViewModel
        .getBalance(LocalDate.now(), TransactionAccountType.SAVINGS)
        .collectAsState(initial = 0)

    val currentTypeSum by transactionViewModel
        .getSumyByType(LocalDate.now())
        .collectAsState(initial = emptyMap())

    val buyIdeas by transactionViewModel
        .getBuyIdeas()
        .collectAsState(initial = emptyList())
    var showSheetBuyIdea by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA69D9D))
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Fin App",
            fontSize = 40.sp,
            modifier = Modifier.padding(10.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(0.85f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            currentTypeSum.forEach { (type, sum) ->
                val categories = transactionViewModel.getSumyByCategories(LocalDate.now(), type)
                TypeBox(
                    name = type.name,
                    amount = sum,
                    categories = categories,
                    navController = navController
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
        Text("Váše zustatky", fontSize = 28.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Normální účet",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 20.sp
                )
                BalanceBox(currentBalanceRegular)
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Spořicí účet",
                    style = MaterialTheme.typography.labelMedium,
                    fontSize = 20.sp
                )
                BalanceBox(currentBalanceSavings)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        GoalBox(
            goalFlow = monthGoalViewModel.getCurrentMonthGoal(),
            savingsFlow = transactionViewModel.getBalance(
                LocalDate.now(),
                TransactionAccountType.SAVINGS
            ),
            investmentFlow = transactionViewModel.getSumForCategory(
                LocalDate.now(),
                TransactionCategory.INVESTMENT
            ),
            onSaveClick = { updatedGoal ->
                monthGoalViewModel.setGoal(updatedGoal)
            }
        )
        Spacer(modifier = Modifier.height(40.dp))

        BuyIdeasDashboard(onBuyIdeaClick = { showSheetBuyIdea = true },buyIdeas,transactionViewModel)
    }
    if (showSheetBuyIdea) {
        ModalBottomSheet(
            onDismissRequest = { showSheetBuyIdea = false },
            sheetState = sheetState,
            containerColor = Color(0xFFD9D9D9),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 300.dp)
            ) {
                BuyIdeaForm(
                    onDismiss = { showSheetBuyIdea = false },
                    viewModel = transactionViewModel
                )
            }
        }
    }

}