package com.example.finamobileapp.view_model.uiState

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.entities.enums.TransactionAccountType

@Immutable
data class BuyIdeaUiState(
    val isChecked: Boolean = false,
    val expandedAccountType: Boolean = false,
    val selectedOptionAccType: TransactionAccountType = TransactionAccountType.REGULAR,
    val isOptionOpen: Boolean = false,
    val isDeleteFormOpen: Boolean = false
)

