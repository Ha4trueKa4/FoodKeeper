package com.example.foodkeeper.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.foodkeeper.domain.Product


@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    isLoading: Boolean = false,
    onDelete: (String) -> Unit,
    onEdit: (String) -> Unit,
    pendingDeleteProductId: String? = null
) {
    when {
        isLoading -> Unit
        products.isEmpty() -> EmptyProductsState(modifier = modifier)
        else -> LazyColumn(modifier = modifier) {
            items(
                items = products,
                key = { it.firebaseId }
            ) { product ->
                SwipeableProductCard(
                    modifier = Modifier.animateItem(
                        fadeOutSpec = tween(durationMillis = 200),
                        placementSpec = spring(stiffness = Spring.StiffnessMedium)
                    ),
                    product = product,
                    onDelete = onDelete,
                    onEdit = onEdit,
                    isPendingDeletion = product.firebaseId == pendingDeleteProductId
                )
            }
        }
    }
}