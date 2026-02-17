package com.example.finamobileapp.models

import androidx.compose.ui.graphics.Color

enum class TransactionCategory(val type: TransactionType, val color: Color) {

    SALARY(TransactionType.INCOME, Color(0xFF4CAF50)),
    PRESENT(TransactionType.INCOME, Color(0xFF8BC34A)),
    ALLOWANCE(TransactionType.INCOME, Color(0xFFCDDC39)),


    SAVINGS(TransactionType.EXPENSE, Color(0xFF2196F3)),
    FUN(TransactionType.EXPENSE, Color(0xFF9C27B0)),
    INVESTMENT(TransactionType.EXPENSE, Color(0xFFFFFF00)),
    FOOD(TransactionType.EXPENSE, Color(0xFFF44336)),
    RENT(TransactionType.EXPENSE, Color(0xFFFF9800)),


    TRANSFER(TransactionType.EXPENSE, Color(0xFF9E9E9E))
}