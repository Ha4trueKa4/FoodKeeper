package com.example.foodkeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.foodkeeper.presentation.screens.AddEditProductScreen
import com.example.foodkeeper.presentation.screens.MainScreen


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
                onEdit = { productId ->
                    navHostController.navigate(Routes.Edit(productId))
                }
            )
        }

        composable<Routes.Edit> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.Edit>()
            AddEditProductScreen(
                productId = route.productId
            ) {
                val navBackStackEntry = navHostController.currentBackStackEntry
                if (navBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                    navHostController.popBackStack()
                }
            }
        }
    }
}