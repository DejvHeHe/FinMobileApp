package com.example.finamobileapp.view_model.interfaces

import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory

sealed interface BuyIdeaActions {
    data class ToggleIsChecked(val id: Long) : BuyIdeaActions
    data class ToggleExpandAccountType(val id: Long) : BuyIdeaActions
    data class SelectAccTypeOption(val id: Long, val accountType: TransactionAccountType) :
        BuyIdeaActions

    data class ToggleOption(val id: Long) : BuyIdeaActions
    data class ToggleDeleteForm(val id: Long?) : BuyIdeaActions
    data class AddTransaction(val buyIdea: BuyIdeas) : BuyIdeaActions
    data class DeleteBuyIdea(val buyIdea: BuyIdeas) : BuyIdeaActions
    object PrepareCreate : BuyIdeaActions
    data class PrepareUpdate(val buyIdea: BuyIdeas) : BuyIdeaActions
    data class SetBuyIdeaSheet(val isOpen: Boolean) : BuyIdeaActions
    data class SetFormName(val name: String) : BuyIdeaActions
    data class SetFormPrice(val price: String) : BuyIdeaActions
    data class SetFormDescription(val description: String) : BuyIdeaActions
    data class SetFormCategory(val category: TransactionCategory) : BuyIdeaActions
    data class SetFormExpandedCategory(val isExpanded: Boolean) : BuyIdeaActions
    object OnBuyIdeaSubmit : BuyIdeaActions


}