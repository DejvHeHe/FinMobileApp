package com.example.finamobileapp.components



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun Footer(
    onAddClick: () -> Unit,
    navController:NavHostController

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Column {
        HorizontalDivider(thickness = 2.dp, color = Color.Black)
        NavigationBar(
            modifier = Modifier.height(70.dp),
            containerColor = Color(0xFFA69D9D)
        ) {

            NavigationBarItem(
                selected = currentRoute=="dashboard",
                onClick ={if(currentRoute!="dashboard") navController.navigate("dashboard")} ,
                icon = { Icon(Icons.Filled.Home, "Domů", modifier = Modifier.size(30.dp)) }
            )


            NavigationBarItem(
                selected = false,
                onClick = onAddClick,
                icon = { Icon(Icons.Filled.Add, "Přidat", modifier = Modifier.size(45.dp)) }
            )


            NavigationBarItem(
                selected = currentRoute=="archive",
                onClick = {if(currentRoute!=="archive") navController.navigate("archive")},
                icon = { Icon(Icons.Filled.DateRange, "Historie", modifier = Modifier.size(30.dp)) }
            )
        }
    }
}