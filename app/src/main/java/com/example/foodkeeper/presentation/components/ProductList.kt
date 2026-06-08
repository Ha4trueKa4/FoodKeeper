package com.example.foodkeeper.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.domain.Product


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    products: List<Product>,
    isLoading: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onDelete: (String) -> Unit,
    onEdit: (String) -> Unit,
    pendingDeleteProductId: String? = null
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        when {
            isLoading -> Unit
            products.isEmpty() -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EmptyProductsState()
            }
            else -> LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)) {
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

}