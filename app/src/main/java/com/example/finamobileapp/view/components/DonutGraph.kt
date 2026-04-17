package com.example.finamobileapp.view.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
// Tvůj vlastní model
import com.example.finamobileapp.model.DonutSegment

@Composable
fun DonutGraph(
    segments: List<DonutSegment>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        var startAngle = -90f

        segments.forEach { segment ->
            Log.d("DonutGraph", "Aktuální počet segmentů: ${segments}")
            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = segment.sweepAngle,
                useCenter = false,
                style = Stroke(width = 40.dp.toPx(), cap = StrokeCap.Butt)
            )
            startAngle += segment.sweepAngle
        }
    }
}



