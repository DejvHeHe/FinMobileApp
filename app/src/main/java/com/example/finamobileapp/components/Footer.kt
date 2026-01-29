package com.example.finamobileapp.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun Footer() {
    Row {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add Transaction"
        )
    }
}