package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    onEdit : (Int) -> Unit,
    onAdd : () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val pendingDeleteProduct by viewModel.pendingDeleteProduct.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val visibleProducts = products

    LaunchedEffect(pendingDeleteProduct) {
        pendingDeleteProduct?.let {
            val result = snackBarHostState.showSnackbar(
                message = "Продукт удалится через 3 секунды",
                actionLabel = "Отменить",
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDeletion()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            AddProductFloatingActionButton {
                onAdd()
            }
        }
    ) { innerPadding ->
        ProductList(
            modifier = Modifier.padding(innerPadding),
            products = visibleProducts,
            onDelete = { productId ->
                val productToDelete = products.find { it.id == productId }
                productToDelete?.let {
                    viewModel.deleteRequest(it)
                }
            },
            onEdit = { productId->
                onEdit(productId)
            },
            pendingDeleteProductId = pendingDeleteProduct?.id
        )
    }
}

