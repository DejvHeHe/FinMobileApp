package com.example.finamobileapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.models.TransactionCategory
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.component.LineComponent

@Composable
fun Graph(graphData: Map<TransactionCategory, Int>) {

    if (graphData.isEmpty()) {
        Text("Žádná data k zobrazení", modifier = Modifier.padding(16.dp))
        return
    }

    val activeCategories = remember(graphData) { graphData.keys.toList() }


    val chartModel = remember(graphData) {
        CartesianChartModel(
            ColumnCartesianLayerModel.build {
                activeCategories.forEach { category ->
                    series(graphData[category]?.toFloat() ?: 0f)
                }
            }
        )
    }


    val columnComponents = remember(activeCategories) {
        activeCategories.map { category ->
            LineComponent(
                color = category.color.toArgb(),
                thicknessDp = 32f
            )
        }
    }

    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberColumnCartesianLayer(
                columnProvider = ColumnCartesianLayer.ColumnProvider.series(columnComponents),
                mergeMode = { ColumnCartesianLayer.MergeMode.Stacked }
            ),
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis()
        ),
        model = chartModel,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}