package com.example.finamobileapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Footer() {
    Column {
        HorizontalDivider(thickness = 2.dp, color = Color.Black)
        NavigationBar(
            modifier = Modifier.height(70.dp),
            containerColor = Color(0xFFA69D9D)
        ) {
            NavigationBarItem(
                selected = false,
                onClick = { /* create formulář */ },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Přidat",
                        modifier = Modifier.size(60.dp),
                        tint = Color.Black
                    )
                }
            )
        }
    }
}