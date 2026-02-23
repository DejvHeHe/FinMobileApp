package com.example.finamobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finamobileapp.view.components.Footer
import com.example.finamobileapp.view.forms.CreateForm
import com.example.finamobileapp.view_model.DashboardViewModel
import com.example.finamobileapp.view.screens.ArchiveScreen
import com.example.finamobileapp.view.screens.CategoryDetail
import com.example.finamobileapp.view.screens.Dashboard


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val transactionViewModel: DashboardViewModel = viewModel()

            var showSheet by remember { mutableStateOf(false) }
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)




            Scaffold(
                bottomBar = {
                    Footer(
                        onAddClick = { showSheet = true },
                        navController = navController,

                        )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "dashboard",
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable("dashboard") {
                        Dashboard(navController, transactionViewModel)
                    }
                    composable("archive") {
                        ArchiveScreen()
                    }
                    composable("CategoryDetail/{categoryName}") { backStackEntry ->
                        val catName =
                            backStackEntry.arguments?.getString("categoryName") ?: "Unknown"
                        CategoryDetail(catName)
                    }
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
                        CreateForm(
                            onDismiss = { showSheet = false },
                            viewModel = transactionViewModel
                        )
                    }
                }
            }
        }
    }
}

