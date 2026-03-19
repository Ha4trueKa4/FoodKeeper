package com.example.foodkeeper.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.foodkeeper.presentation.model.Product

@Composable
fun ProductList(modifier: Modifier = Modifier, products: List<Product>, onDelete : (Product) -> Unit) {
    LazyColumn(
        modifier = modifier
    ) {
        items(products) {
            product -> ProductCard(product, onDelete = onDelete)
        }
    }
}