package com.example.foodkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.foodkeeper.presentation.screens.AddEditProductScreen
import com.example.foodkeeper.presentation.screens.AuthScreen
import com.example.foodkeeper.presentation.screens.MainScreen
import com.example.foodkeeper.presentation.screens.SettingsScreen
import com.example.foodkeeper.presentation.navigation.Routes

@Composable
fun FoodKeeperNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Routes.Auth
    ) {

        composable<Routes.Add> {
            AddEditProductScreen(
                productId = null
            ) {
                val navBackStackEntry = navHostController.currentBackStackEntry
                if (navBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                    navHostController.popBackStack()
                }
            }
        }
        composable<Routes.Main> {
            MainScreen(
                onAdd = {
                    navHostController.navigate(Routes.Add)
                },
                onEdit = { firebaseId ->
                    navHostController.navigate(Routes.Edit(firebaseId))
                },
                onSettings = {
                    navHostController.navigate(Routes.Settings)
                }
            )
        }

        composable<Routes.Edit> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.Edit>()
            AddEditProductScreen(
                productId = route.firebaseId
            ) {
                val navBackStackEntry = navHostController.currentBackStackEntry
                if (navBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                    navHostController.popBackStack()
                }
            }
        }

        composable<Routes.Auth> {
            AuthScreen {
                navHostController.navigate(Routes.Main) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable<Routes.Settings> {
            SettingsScreen(
                onBack = { navHostController.popBackStack() },
                onLogout = {
                    navHostController.navigate(Routes.Auth) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}