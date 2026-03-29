package com.example.foodkeeper.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Add : Routes()
    @Serializable
    data object Main : Routes()
}