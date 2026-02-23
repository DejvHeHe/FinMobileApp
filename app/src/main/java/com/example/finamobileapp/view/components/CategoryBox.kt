package com.example.finamobileapp.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finamobileapp.model.entities.enums.TransactionCategory

@Composable
fun CategoryBox(
    name: TransactionCategory,
    amount: Int,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.90f)
            .clickable {
                navController.navigate("CategoryDetail/${name.name}")
            }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${name.name}:", fontSize = 18.sp)
        Text(text = "$amount Kč", fontSize = 18.sp)
    }
}