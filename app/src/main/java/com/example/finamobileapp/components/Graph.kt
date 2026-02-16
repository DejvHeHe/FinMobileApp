package com.example.finamobileapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.TransactionCategory

// Importy pro Vico 2.0-alpha.28
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.shape.Shape

@Composable
fun Graph() {
    val categoryColors = mapOf(
        TransactionCategory.FOOD to Color(0xFFF44336),
        TransactionCategory.SAVINGS to Color(0xFF2196F3),
        TransactionCategory.FUN to Color(0xFF4CAF50),
        TransactionCategory.RENT to Color(0xFFFF9800),
        TransactionCategory.INVESTMENT to Color(0xFFFFFF00)
    )

    val testData = mapOf(
        TransactionCategory.FOOD to 2000f,
        TransactionCategory.SAVINGS to 5000f,
        TransactionCategory.FUN to 5000f,
        TransactionCategory.RENT to 0f,
        TransactionCategory.INVESTMENT to 5000f
    )

    val activeCategories = testData.keys.toList()
    val amounts = activeCategories.map { testData[it] ?: 0f }
    val colors = activeCategories.map { categoryColors[it] ?: Color.Gray }

    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(testData) {
        modelProducer.runTransaction {
            columnSeries {
                amounts.forEach { series(it) }
            }
        }
    }

    // Tady definujeme komponenty sloupce dopředu, aby se předešlo chybám v typech uvnitř DSL
    val columnComponents = colors.map { color ->
        rememberLineComponent(
            color = color,
            thickness = 64.dp,
            shape = Shape.Rectangle
        )
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                    *columnComponents.toTypedArray()
                ),
                mergeMode = { ColumnCartesianLayer.MergeMode.Stacked }
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = { _, _, _ -> "Tento měsíc" }
            )
        ),
        modelProducer = modelProducer,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(horizontal = 48.dp)
    )
}