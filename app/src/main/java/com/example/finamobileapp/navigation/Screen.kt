package com.example.finamobileapp.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen() {
    @Serializable
    object Dashboard : Screen()
    @Serializable
    object Archive : Screen()
    @Serializable
    data class CategoryDetail (
        val categoryName:String

    ): Screen()
}