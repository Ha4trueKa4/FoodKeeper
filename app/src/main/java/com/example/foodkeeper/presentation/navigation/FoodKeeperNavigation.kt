package com.example.foodkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodkeeper.presentation.screens.AddProductScreen

import com.example.foodkeeper.presentation.screens.LoginScreen
import com.example.foodkeeper.presentation.screens.MainScreen
import com.example.foodkeeper.presentation.screens.RegisterScreen


@Composable
fun FoodKeeperNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.Main
    ) {

        composable<Routes.Add> {
            AddProductScreen { }
        }

        composable<Routes.Main> {
            MainScreen()
        }
    }
}