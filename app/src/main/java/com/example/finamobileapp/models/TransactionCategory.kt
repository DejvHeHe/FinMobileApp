package com.example.finamobileapp.models

enum class TransactionCategory(val type: TransactionType) {
    //INCOME
    SALARY(TransactionType.INCOME),
    PRESENT(TransactionType.INCOME),
    ALLOWANCE(TransactionType.INCOME),

    //EXPENSE
    SAVINGS(TransactionType.EXPENSE),
    FUN(TransactionType.EXPENSE),
    INVESTMENT(TransactionType.EXPENSE),
    FOOD(TransactionType.EXPENSE),
    RENT(TransactionType.EXPENSE),


    //TRANSAFER
    TRANSFER(TransactionType.EXPENSE),

}