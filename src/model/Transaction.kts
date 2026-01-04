package models
import models.TransactionType
import models.TransactionCategory
data class Transaction(
    val id: Long,
    val amount: Int,
    val name: String,
    val category: TransactionCategory,
    val type: TransactionType
)
