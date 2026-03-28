package com.example.foodkeeper.presentation.screens

import android.app.Application
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.usecases.AddProductUseCase
import com.example.foodkeeper.domain.usecases.DeleteProductUseCase
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import com.example.foodkeeper.presentation.components.AddProductFloatingActionButton
import com.example.foodkeeper.presentation.components.ProductList

import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModelFactory

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = viewModel(factory = FoodKeeperViewModelFactory(LocalContext.current.applicationContext as Application))
) {
    viewModel.fetchProducts()
    val products by viewModel.products.collectAsState()

    Scaffold(
        floatingActionButton = {
            AddProductFloatingActionButton {
                viewModel.addProduct(
                    Product(
                        id = 0,
                        name = "Новый продукт",
                        expiryDate = "01.01.2026",
                        imageUrl = ""
                    )
                )
            }
        }
    ) { innerPadding ->
        ProductList(modifier = Modifier.padding(innerPadding), products = products, onDelete = {
            viewModel.deleteProduct(it.id)
        })
    }


}

