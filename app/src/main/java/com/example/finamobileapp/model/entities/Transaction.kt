package com.example.finamobileapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finamobileapp.model.entities.enums.TransactionAccountType
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType
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
    val description: String,
    val groupId:String?
)