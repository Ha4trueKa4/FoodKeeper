package com.example.foodkeeper.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Auth : Routes()

    @Serializable
    data object Main : Routes()

    @Serializable
    data object Add : Routes()

    @Serializable
    data class Edit(val firebaseId: String) : Routes()
}