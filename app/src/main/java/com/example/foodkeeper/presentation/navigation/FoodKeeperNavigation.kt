package com.example.foodkeeper.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.foodkeeper.presentation.screens.AddEditProductScreen
import com.example.foodkeeper.presentation.screens.AuthScreen
import com.example.foodkeeper.presentation.screens.MainScreen
import com.example.foodkeeper.presentation.screens.SettingsScreen
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodKeeperNavigation(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = if (FirebaseAuth.getInstance().currentUser != null) Routes.Main else Routes.Auth
    ) {
        composable<Routes.Add> {
            AddEditProductScreen(productId = null) {
                navHostController.popBackStack()
            }
        }

        composable<Routes.Main> {
            val viewModel: FoodKeeperViewModel = koinViewModel()

            LaunchedEffect(Unit) {
                viewModel.syncNow()
            }
            MainScreen(
                onAdd = { navHostController.navigate(Routes.Add) },
                onEdit = { firebaseId -> navHostController.navigate(Routes.Edit(firebaseId)) },
                onSettings = { navHostController.navigate(Routes.Settings) }
            )
        }

        composable<Routes.Edit> { backStackEntry ->
            val route = backStackEntry.toRoute<Routes.Edit>()
            AddEditProductScreen(productId = route.firebaseId) {
                navHostController.popBackStack()
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