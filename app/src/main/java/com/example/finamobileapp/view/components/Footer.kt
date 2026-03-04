package com.example.finamobileapp.view.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.finamobileapp.navigation.Screen

@Composable
fun Footer(
    onAddClick: () -> Unit,
    onTabSelect: (String) -> Unit
) {
    Column {
        HorizontalDivider(thickness = 2.dp, color = Color.Black)
        NavigationBar(
            modifier = Modifier.height(70.dp),
            containerColor = Color(0xFFA69D9D)
        ) {
            // Dashboard
            FooterItem(
                icon = Icons.Filled.Home,
                onClick = { onTabSelect("dashboard") }
            )

            // Add Button
            FooterItem(
                icon = Icons.Filled.Add,
                iconSize = 50.dp,
                onClick = onAddClick
            )

            // Archive
            FooterItem(
                icon = Icons.Filled.DateRange,
                onClick = { onTabSelect("archive") }
            )
        }
    }
}


@Composable
private fun RowScope.FooterItem(
    icon: ImageVector,
    isSelected: Boolean = false,
    iconSize: Dp = 35.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(iconSize),
            tint = if (isSelected) Color.Black else Color.DarkGray
        )
    }
}