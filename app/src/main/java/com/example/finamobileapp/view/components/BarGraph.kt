package com.example.finamobileapp.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.view_model.uiState.MonthlyStats

@Composable
fun BarGraph(
    quartalList: List<MonthlyStats>,
    quartalNames: List<String>
) {
    val maxVal = remember(quartalList) {
        quartalList.flatMap { listOf(it.expenses.toFloat(), it.income.toFloat()) }
            .maxOrNull() ?: 1f
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        quartalList.forEachIndexed { index, stats ->
            BarGroup(
                label = quartalNames.getOrElse(index) { "" },
                expenses = stats.expenses.toFloat(),
                income = stats.income.toFloat(),
                maxValue = maxVal
            )
        }
    }
}

@Composable
private fun BarGroup(
    label: String,
    expenses: Float,
    income: Float,
    maxValue: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.weight(1f) // Aby se skupina roztáhla
        ) {
            // Výdaje (Červená)
            SimpleBar(value = expenses, maxValue = maxValue, color = Color(0xFFEF5350))
            Spacer(modifier = Modifier.width(4.dp))
            // Příjmy (Zelená)
            SimpleBar(value = income, maxValue = maxValue, color = Color(0xFF66BB6A))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun SimpleBar(
    value: Float,
    maxValue: Float,
    color: Color
) {
    val fillRatio = (value / maxValue).coerceIn(0.01f, 1f)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(40.dp)
    ) {

        Text(
            text = value.toInt().toString(),
            style = MaterialTheme.typography.labelSmall,
            //modifier = Modifier.rotate(-90f)
        )

        Spacer(modifier = Modifier.height(8.dp))


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fillRatio)
                .background(color, shape = MaterialTheme.shapes.extraSmall)
        )
    }
}