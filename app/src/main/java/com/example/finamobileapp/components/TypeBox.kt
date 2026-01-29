package com.example.finamobileapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.models.TypeBoxData

@Composable
fun TypeBox(data: TypeBoxData,modifier: Modifier = Modifier)
{
    Card(
        modifier =modifier
            .fillMaxWidth(0.50f),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {

            Text(text = data.title, fontSize = 20.sp, modifier = Modifier.padding(20.dp), textAlign = TextAlign.Center)
            data.items.forEach { (category, sum) ->

                CategoryBox(category, sum)
                HorizontalDivider(modifier = Modifier.padding(5.dp).fillMaxWidth(), thickness = 1.dp, color=Color.Black)
            }
            Text(text = "= ${data.totalSum}")

        }


    }

}