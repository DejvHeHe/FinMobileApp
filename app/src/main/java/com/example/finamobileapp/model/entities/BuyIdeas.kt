package com.example.finamobileapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.finamobileapp.model.entities.enums.TransactionCategory
import com.example.finamobileapp.model.entities.enums.TransactionType

@Entity(tableName = "buy_ideas_table")
data class BuyIdeas(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Int,
    val category: TransactionCategory,
    val type: TransactionType,
    val description: String
)



