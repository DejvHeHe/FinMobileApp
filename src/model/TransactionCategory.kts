package models
import models.TransactionType
enum class TransactionCategory(val type: TransactionType) {

    // INCOME
    PART_TIME_JOB(TransactionType.INCOME),
    ALLOWANCE(TransactionType.INCOME),
    SIDE_JOB(TransactionType.INCOME),
    PRESENT_MONEY(TransactionType.INCOME),

    // EXPENSE
    FOOD(TransactionType.EXPENSE),
    PRESENT(TransactionType.EXPENSE),
    FUN(TransactionType.EXPENSE),
    CLOTHES(TransactionType.EXPENSE),
    RENT(TransactionType.EXPENSE),
    SCHOOL(TransactionType.EXPENSE),
    INVESTMENT(TransactionType.EXPENSE),
    SAVINGS(TransactionType.EXPENSE),
    VACATION(TransactionType.EXPENSE)
}
