package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foodkeeper.presentation.components.AddProductFloatingActionButton
import com.example.foodkeeper.presentation.components.ProductList
import com.example.foodkeeper.presentation.navigation.Routes
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = koinViewModel(),
    navHostController: NavController
) {
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val products by viewModel.products.collectAsState()

    Scaffold(
        floatingActionButton = {
            AddProductFloatingActionButton {
                navHostController.navigate(Routes.Add)
            }
        }
    ) { innerPadding ->
        ProductList(modifier = Modifier.padding(innerPadding), products = products, onDelete = {
            viewModel.deleteProduct(it.id)
        })
    }
}

