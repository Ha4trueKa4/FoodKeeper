package com.example.foodkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
        startDestination = Routes.Login
    ) {
        composable<Routes.Login> {
            LoginScreen { navigateTo ->
                navHostController.navigate(navigateTo)
            }
        }
        composable<Routes.Register> {
            RegisterScreen{ navigateTo ->
                navHostController.navigate(navigateTo)
            }
        }
        composable<Routes.Main> {
            MainScreen{ navigateTo ->
                navHostController.navigate(navigateTo)
            }
        }
    }
}