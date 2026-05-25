package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    onEdit : (Int) -> Unit,
    onAdd : () -> Unit,
    onLogout: () -> Unit = {}
) {
    val products by viewModel.products.collectAsState()
    val pendingDeleteProduct by viewModel.pendingDeleteProduct.collectAsState()

    val isLoadingList by viewModel.isLoadingList.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }

    val visibleProducts = products

    LaunchedEffect(pendingDeleteProduct) {
        pendingDeleteProduct?.let { product ->
            snackBarHostState.currentSnackbarData?.dismiss()
            val job = launch {
                val result = snackBarHostState.showSnackbar(
                    message = "${product.name} будет удалён",
                    actionLabel = "Отменить",
                    duration = SnackbarDuration.Indefinite,
                    withDismissAction = false
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.cancelDelete()
                }
            }
            delay(3000)

            job.cancel()
            snackBarHostState.currentSnackbarData?.dismiss()
            viewModel.confirmDelete()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FoodKeeper") },
                actions = {
                    IconButton(onClick = {
                        authViewModel.signOut()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, contentDescription = "Выйти из аккаунта")
                    }
                }
            )
        },
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
            isLoading = isLoadingList,
            onDelete = { productId ->
                val productToDelete = products.find { it.id == productId }
                productToDelete?.let {
                    viewModel.requestDelete(it)
                }
            },
            onEdit = { productId->
                onEdit(productId)
            },
            pendingDeleteProductId = pendingDeleteProduct?.id
        )
    }
}

