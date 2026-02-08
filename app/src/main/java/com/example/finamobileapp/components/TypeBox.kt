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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finamobileapp.models.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Composable
fun TypeBox(
    name: String,
    amount: Int,
    categories: Flow<Map<TransactionCategory, Int>>,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {

    val cats by categories.collectAsState(initial = emptyMap())

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {

            Text(
                text = name,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                textAlign = TextAlign.Center
            )


            cats.forEach { (category, sum) ->

                CategoryBox(category, sum,navController)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            Text(
                text = "= $amount",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                fontSize = 18.sp
            )
        }
    }
}