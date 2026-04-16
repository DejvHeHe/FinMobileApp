package com.example.finamobileapp.model.entities.enums

enum class TransactionCategory(val type: TransactionType) {

    SALARY(TransactionType.INCOME),
    ALLOWANCE(TransactionType.INCOME),

    SAVINGS(TransactionType.EXPENSE),
    FUN(TransactionType.EXPENSE),
    INVESTMENT(TransactionType.EXPENSE),
    FOOD(TransactionType.EXPENSE),
    RENT(TransactionType.EXPENSE),
    CLOTHES(TransactionType.EXPENSE),
    DRUGS(TransactionType.EXPENSE),
    VET(TransactionType.EXPENSE),
    PRESENT(TransactionType.EXPENSE),
    HEALTH(TransactionType.EXPENSE),
    GYM(TransactionType.EXPENSE),
    OTHER(TransactionType.EXPENSE),


    TRANSFER(TransactionType.EXPENSE)
}