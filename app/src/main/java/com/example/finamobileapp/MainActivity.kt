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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.finamobileapp.view.components.Footer
import com.example.finamobileapp.view.forms.CreateForm
import com.example.finamobileapp.view.screens.ArchiveScreen
import com.example.finamobileapp.view.screens.CategoryDetail
import com.example.finamobileapp.view.screens.Dashboard
import com.example.finamobileapp.view_model.DashboardViewModel
// ZDE PŘEDPOKLÁDÁM IMPORT TVÉHO VIEWMODELU
import com.example.finamobileapp.view_model.MainActivityViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Použití tvého externího ViewModelu
            val mainVm: MainActivityViewModel = viewModel()
            val mainState by mainVm.uiState.collectAsState()

            // Navigace
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

            // ViewModel pro Dashboard/Formulář
            val transactionViewModel: DashboardViewModel = viewModel()
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            Scaffold(
                bottomBar = {
                    Footer(
                        currentRoute = currentRoute,
                        onAddClick = { mainVm.toggleCreateForm() },
                        onTabSelect = { route ->
                            // Vždy čistíme stack, aby se navigace chovala předvídatelně
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
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
                        val catName = backStackEntry.arguments?.getString("categoryName") ?: "Unknown"
                        CategoryDetail(catName)
                    }
                }
            }

            if (mainState.isCreateFormOpen) {
                ModalBottomSheet(
                    onDismissRequest = { mainVm.toggleCreateForm() },
                    sheetState = sheetState,
                    containerColor = Color(0xFFD9D9D9),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 300.dp)
                    ) {
                        CreateForm(
                            onDismiss = { mainVm.toggleCreateForm() },
                            viewModel = transactionViewModel
                        )
                    }
                }
            }
        }
    }
}