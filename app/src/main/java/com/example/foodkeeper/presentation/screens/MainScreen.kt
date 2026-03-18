package com.example.foodkeeper.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodkeeper.presentation.components.ProductList
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: FoodKeeperViewModel = viewModel()
) {
    val products by viewModel.products.collectAsState()

    ProductList(products= products)
}