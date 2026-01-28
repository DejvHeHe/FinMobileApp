package com.example.finamobileapp.models

import java.time.LocalDate
import java.util.UUID


data class Transaction (
    val id: UUID,
    val name: String,
    val amount: Int,
    val category: TransactionCategory,
    val type: TransactionType,
    val date: LocalDate,
    val description: String
)
