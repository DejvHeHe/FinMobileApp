package com.example.finamobileapp.view_model.uiState

import androidx.compose.runtime.Immutable
import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.enums.FormMode
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory

@Immutable
data class BuyIdeaUiState(

    val isOpen: Boolean = false,
    val mode: FormMode = FormMode.CREATE,
    val selectedIdea: BuyIdeas? = null,


    val isChecked: Set<Long> = emptySet(),
    val expandedAccountType: Set<Long> = emptySet(),
    val selectedOptionAccType: Map<Long, TransactionAccountType> = emptyMap(),
    val isOptionOpen: Long? = null,
    val isDeleteFormOpen: Long? = null,


    //BuyIdeaForm
    val formName: String = "",
    val formPrice: String = "",
    val formDescription: String = "",
    val formCategory: TransactionCategory? = null,
    val formExpandedCategory: Boolean = false


)

