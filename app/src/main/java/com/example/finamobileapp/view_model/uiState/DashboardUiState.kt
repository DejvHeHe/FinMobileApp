package com.example.finamobileapp.view_model.uiState

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.enums.FormMode
import com.example.finamobileapp.model.entities.enums.TransactionType

@Immutable
data class DashboardUiState(

    val balanceRegular: Int = 0,
    val balanceSavings: Int = 0,
    val typeSums: Map<TransactionType, Int> = emptyMap(),
    val buyIdeas: List<BuyIdeas> = emptyList(),
    val isLoading: Boolean = false,

    val isBuyIdeaSheetOpen: Boolean = false,
    val buyIdeaFormMode: FormMode = FormMode.CREATE,
    val selectedBuyIdea: BuyIdeas? = null,

    )

