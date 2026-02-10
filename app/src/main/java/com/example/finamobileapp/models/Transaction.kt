package com.example.finamobileapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity(tableName = "transactions_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val amount: Int,
    val category: TransactionCategory,
    val type: TransactionType,
    val accountType: TransactionAccountType,
    val date: LocalDate,
    val description: String
)