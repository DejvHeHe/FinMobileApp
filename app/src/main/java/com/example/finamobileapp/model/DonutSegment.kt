package com.example.finamobileapp.model

import androidx.compose.ui.graphics.Color
import com.example.finamobileapp.model.entities.enums.TransactionCategory

data class DonutSegment(
    val category: TransactionCategory,
    val amount: Int,
    val sweepAngle: Float,
    val color: Color
)
