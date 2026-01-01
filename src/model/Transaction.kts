data class Transaction(
    val id: Long,
    val amount: Int,
    val name: String,
    val category: String,
    val type: TransactionType
)
