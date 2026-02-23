package com.example.finamobileapp.view_model

import com.example.finamobileapp.model.entities.Transaction

data class CategoryDetailUiState(
    // --- Data ---
    val transactions: List<Transaction> = emptyList(),
    val selectedTransaction: Transaction? = null,

    // --- Stavy viditelnosti
    val expandedTransactionId: Long? = null,
    val isOptionsOpen: Boolean = false,
    val isDeleteFormOpen: Boolean = false,
    val isUpdateFormOpen: Boolean = false,
    

    // --- Logický stav ---
    val isRecurringAction: Boolean = false,


    val isLoading: Boolean = false
)
