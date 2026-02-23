package com.example.finamobileapp.model.entities.enums

enum class TransactionCategory(val type: TransactionType) {

    SALARY(TransactionType.INCOME),
    PRESENT(TransactionType.INCOME),
    ALLOWANCE(TransactionType.INCOME),

    SAVINGS(TransactionType.EXPENSE),
    FUN(TransactionType.EXPENSE),
    INVESTMENT(TransactionType.EXPENSE),
    FOOD(TransactionType.EXPENSE),
    RENT(TransactionType.EXPENSE),


    TRANSFER(TransactionType.EXPENSE)
}