package com.example.foodkeeper.presentation.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodkeeper.presentation.model.Product

@Composable
fun ProductList(modifier: Modifier = Modifier, products: List<Product>) {
    LazyColumn {
        items(products) {
            product -> ProductCard(product)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductListPreview(modifier: Modifier = Modifier) {
    ProductList(
        products = listOf(
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
        )

    )
}