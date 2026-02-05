package com.example.finamobileapp

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
import java.time.LocalDate

// --- IMPORTY PRO NAVIGACI ---
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// ----------------------------

import com.example.finamobileapp.components.BalanceBox
import com.example.finamobileapp.components.Footer
import com.example.finamobileapp.components.TypeBox
import com.example.finamobileapp.components.forms.CreateForm
import com.example.finamobileapp.models.view_model.TransactionViewModel
import com.example.finamobileapp.screens.CategoryDetail // Ujisti se, že máš CategoryDetail v tomto balíčku

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Inicializace navigace
            val navController = rememberNavController()

            // Definice tras aplikace
            NavHost(
                navController = navController,
                startDestination = "dashboard"
            ) {
                // Hlavní obrazovka
                composable("dashboard") {
                    Dashboard(navController)
                }

                // Detail kategorie s parametrem
                composable("CategoryDetail/{categoryName}") { backStackEntry ->
                    val catName = backStackEntry.arguments?.getString("categoryName") ?: "Unknown"
                    CategoryDetail(catName)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(navController: NavHostController) {
    val transactionViewModel: TransactionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    var showSheet by remember { mutableStateOf(false) }

    // Sběr dat z ViewModelu
    val currentBalance by transactionViewModel
        .getBalance(LocalDate.now())
        .collectAsState(initial = 0)

    val currentTypeSum by transactionViewModel
        .getSumyByType(LocalDate.now())
        .collectAsState(initial = emptyMap())

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

            Text(
                text = "Fin App",
                fontSize = 40.sp,
                modifier = Modifier.padding(10.dp)
            )

            // Horní řada s TypeBoxy (Příjmy / Výdaje)
            Row(
                modifier = Modifier.fillMaxWidth(0.85f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                currentTypeSum.forEach { (type, sum) ->
                    val categories = transactionViewModel.getSumyByCategories(LocalDate.now(), type)

                    TypeBox(
                        name = type.name,
                        amount = sum,
                        categories = categories,
                        modifier = Modifier.weight(1f),
                        navController

                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text("Váš zůstatek", fontSize = 28.sp)
            BalanceBox(currentBalance)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Bottom Sheet pro přidání transakce
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