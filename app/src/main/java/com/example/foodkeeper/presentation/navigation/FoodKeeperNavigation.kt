package com.example.foodkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.foodkeeper.presentation.screens.AddEditProductScreen
import com.example.foodkeeper.presentation.screens.AuthScreen
import com.example.foodkeeper.presentation.screens.MainScreen

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
                onLogout = {
                    navHostController.navigate(Routes.Auth) {
                        popUpTo(0) { inclusive = true }
                    }
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
    }
}