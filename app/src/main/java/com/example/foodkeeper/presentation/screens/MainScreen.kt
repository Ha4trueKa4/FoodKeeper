package com.example.foodkeeper.presentation.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foodkeeper.presentation.components.AddProductFloatingActionButton
import com.example.foodkeeper.presentation.components.ProductList
import com.example.foodkeeper.presentation.filters.FilterBottomSheet
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
    onEdit : (String) -> Unit,
    onAdd : () -> Unit,
    onSettings : () -> Unit
) {
    val products by viewModel.products.collectAsState()
    val pendingDeleteProduct by viewModel.pendingDeleteProduct.collectAsState()
    val isLoadingList by viewModel.isLoadingList.collectAsState()
    val filter by viewModel.filter.collectAsState()

    var showFilterSheet by rememberSaveable { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    val isSyncing by viewModel.isSyncing.collectAsState()

    LaunchedEffect(pendingDeleteProduct) {
        pendingDeleteProduct?.let { product ->
            snackBarHostState.currentSnackbarData?.dismiss()
            val result = snackBarHostState.showSnackbar(
                message = "${product.name} будет удалён",
                actionLabel = "Отменить",
                duration = SnackbarDuration.Indefinite,
                withDismissAction = false
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.cancelDelete()
            } else {
                viewModel.confirmDelete()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FoodKeeper") },
                actions = {
                    BadgedBox(
                        badge = {
                            if (filter.isActive) Badge()
                        }
                    ) {
                        IconButton(onClick = { showFilterSheet = true }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Фильтры")
                        }
                    }
                    IconButton(
                        onClick = { viewModel.syncNow() },
                        enabled = !isSyncing
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Sync, contentDescription = "Синхронизировать")
                        }
                    }
                    IconButton(onClick = { onSettings() }) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
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
            products = products,
            isLoading = isLoadingList,
            onDelete = { firebaseId ->
                val productToDelete = products.find { it.firebaseId == firebaseId }
                productToDelete?.let {
                    viewModel.requestDelete(it)
                }
            },
            onEdit = { firebaseId ->
                onEdit(firebaseId)
            },
            pendingDeleteProductId = pendingDeleteProduct?.firebaseId
        )

    }

    if (showFilterSheet) {
        FilterBottomSheet(
            filter = filter,
            onFilterChange = { viewModel.setFilter(it) },
            onReset = { viewModel.resetFilter() },
            onDismiss = { showFilterSheet = false }
        )
    }
}

