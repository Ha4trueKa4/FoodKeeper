package com.example.foodkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodkeeper.presentation.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FoodKeeperViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(listOf(
        Product(
            id = 1,
            name = "Молоко Простоквашино",
            expiryDate = "12.11.2008",
            imageUrl = "https://google.com"
        ),
        Product(
            id = 1,
            name = "Бананы",
            expiryDate = "30.11.2008",
            imageUrl = "https://google.com"
        ),
        Product(
            id = 1,
            name = "lorem ipsum alsfllf kalklall alkfka klfkqk fqk k",
            expiryDate = "11.03.2007",
            imageUrl = "https://google.com"
        )
    ))
    val products : StateFlow<List<Product>> = _products

    fun addProduct(product: Product) {
        _products.value = _products.value + product
    }

    fun deleteProduct(product: Product) {
        _products.value = _products.value - product
    }
}