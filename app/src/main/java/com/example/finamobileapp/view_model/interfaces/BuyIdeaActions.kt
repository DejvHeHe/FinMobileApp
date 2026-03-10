package com.example.finamobileapp.view_model.interfaces

import com.example.finamobileapp.model.entities.BuyIdeas
import com.example.finamobileapp.model.entities.Transaction
import com.example.finamobileapp.model.entities.enums.TransactionAccountType

sealed interface BuyIdeaActions {
    data class ToggleIsChecked(val id: Long): BuyIdeaActions
    data class ToggleExpandAccountType(val id: Long): BuyIdeaActions
    data class SelectAccTypeOption(val id: Long, val accountType: TransactionAccountType):BuyIdeaActions
    data class ToggleOption(val id: Long): BuyIdeaActions
    data class ToggleDeleteForm(val id: Long?): BuyIdeaActions
    data class AddTransaction(val transaction: Transaction): BuyIdeaActions
    data class DeleteBuyIdea(val buyIdea: BuyIdeas): BuyIdeaActions
    data class SetBuyIdea(val buyIdea: BuyIdeas): BuyIdeaActions
}