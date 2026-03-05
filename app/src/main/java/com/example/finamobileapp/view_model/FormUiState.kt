package com.example.finamobileapp.view_model

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory

@Immutable
data class FormUiState(
    val name: String = "",
    val amount: String = "",
    val description: String = "",
    val isRecurring: Boolean = false,


    val selectedCategory: TransactionCategory? = null,
    val selectedAccountType: TransactionAccountType? = null,


    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,


    val isCategoryExpanded: Boolean = false,
    val isAccountTypeExpanded: Boolean = false,
    val showStartDatePicker: Boolean = false,
    val showEndDatePicker: Boolean = false

)
