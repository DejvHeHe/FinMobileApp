package com.example.finamobileapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BuyIdeasDashboard() {
    Box()
    {
        Column {
            Text("Vaše nápady na nákup")


            Button(onClick = {/**/ }) {
                Text("Vytvořit nový nápad na nákup")
            }

        }
    }
}