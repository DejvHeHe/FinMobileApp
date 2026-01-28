package com.example.finamobileapp.models

data class TypeBoxData(
    val title: String,
    val items: Map<TransactionCategory, Int>,
    val totalSum: Int
)
