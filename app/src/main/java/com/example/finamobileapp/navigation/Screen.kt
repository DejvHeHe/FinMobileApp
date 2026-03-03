package com.example.finamobileapp.navigation

import kotlinx.serialization.Serializable


sealed class Screen(val route: String) {
    @Serializable
    object Dashboard : Screen("dashboard")
    @Serializable
    object Archive : Screen("archive")


    @Serializable
    object CategoryDetail : Screen("category_detail/{categoryName}") {
        // Funkce pro volání v kódu (Dashboard -> Detail)
        fun createRoute(categoryName: String) = "category_detail/$categoryName"
    }
}