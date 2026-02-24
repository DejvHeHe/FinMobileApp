package com.example.finamobileapp.view_model

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.entities.Transaction

@Immutable
data class CategoryDetailUiState(
    // --- Data ---
    val transactions: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction? = null,

    // --- Stavy viditelnosti
    val expandedTransactionId: Long? = null,
    val isOptionsOpen: Boolean = false,
    val isDeleteFormOpen: Boolean = false,
    val isUpdateFormOpen: Boolean = false,
    val expandedCategory: Boolean = false,
    val showStartDatePicker: Boolean = false,


    // --- Logický stav ---
    val isRecurringAction: Boolean = false,


    val isLoading: Boolean = false,

    //Stavy proměných
    val name: String = "",
    val amount: String = "",
    val selectedOption: String = "",
    val description: String = "",
    //val selectedDateMillis: Long?


//    var name by remember { mutableStateOf(transaction.name) }
//var amount by remember { mutableStateOf(transaction.amount.toString()) }
//var expanded by remember { mutableStateOf(false) }
//var selectedOption by remember { mutableStateOf(transaction.category.name) }
//var description by remember { mutableStateOf(transaction.description) }
//
//

//var showStartDatePicker by remember { mutableStateOf(false) }
)
