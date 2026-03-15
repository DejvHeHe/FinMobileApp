package com.example.finamobileapp.view.screens

import android.util.Log
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.finamobileapp.view.components.BalanceBox
import com.example.finamobileapp.view.components.BuyIdeasDashboard
import com.example.finamobileapp.view.components.GoalBox
import com.example.finamobileapp.view.components.TypeBox
import com.example.finamobileapp.view.forms.BuyIdeaForm
import com.example.finamobileapp.view_model.DashboardViewModel
import com.example.finamobileapp.view_model.interfaces.BuyIdeaActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavHostController) {

    val scrollState = rememberScrollState()
    val viewModel: DashboardViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val buyIdeaUisState by viewModel.buyIdeaUiState.collectAsStateWithLifecycle()
    val goalUiState by viewModel.goalUiState.collectAsStateWithLifecycle()


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
            state.typeSums.forEach { (type, sum) ->
                val categories = viewModel.getSumyByCategories(type)
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
                BalanceBox(state.balanceRegular)
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
                BalanceBox(state.balanceSavings)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        GoalBox(
            state.currentGoal,
            state.balanceSavings,
            state.currentlyInvested,
            goalUiState = goalUiState,
            onSaveClick = { updatedGoal ->
                viewModel.setGoal(updatedGoal)
            }
        )
        Spacer(modifier = Modifier.height(40.dp))


        BuyIdeasDashboard(
            buyIdeas = state.buyIdeas,
            onBuyIdeaAction = viewModel::onBuyIdeaAction,
            buyIdeaUiState = buyIdeaUisState
        )
    }
    if (buyIdeaUisState.isOpen) {
        Log.d("DashboardVM", "VYBRANÝ BUYiDEA: ${buyIdeaUisState.selectedIdea}")
        Log.d("DashboardVM", "VYBRANÁ FORMA: ${buyIdeaUisState.mode}")

        ModalBottomSheet(
            onDismissRequest = { viewModel.onBuyIdeaAction(BuyIdeaActions.SetBuyIdeaSheet(false)) },
            sheetState = sheetState,
            containerColor = Color(0xFFD9D9D9),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 300.dp)
            ) {

                BuyIdeaForm(
                    onBuyIdeaActions = viewModel::onBuyIdeaAction,
                    buyIdeaUiState = buyIdeaUisState
                )


            }
        }
    }

}