package com.example.finamobileapp.model.entities

import androidx.room.Entity

@Entity(
    tableName = "monthly_goals",
    primaryKeys = ["year", "month"]
)
data class MonthlyGoal(
    val year: Int,
    val month: Int,
    val savingsGoal: Int = 0,
    val investmentGoal: Int = 0
)