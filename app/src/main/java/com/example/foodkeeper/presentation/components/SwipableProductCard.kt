package com.example.foodkeeper.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.domain.Product


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwipeableProductCard(
    modifier: Modifier = Modifier,
    product: Product,
    onDelete: (String) -> Unit,
    onEdit: (String) -> Unit,
    isPendingDeletion : Boolean = false
) {
    val currentIsPending by rememberUpdatedState(isPendingDeletion)
    val currentOnDelete by rememberUpdatedState(onDelete)



    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {value ->
            if (value == SwipeToDismissBoxValue.EndToStart && !currentIsPending) {
                currentOnDelete(product.firebaseId)
            }
            false
        },
        positionalThreshold = {totalDistance -> totalDistance * 0.4f}
    )
    val bgColor by animateColorAsState(
        targetValue = when (dismissState.targetValue) {
            SwipeToDismissBoxValue.EndToStart -> Color(0xFFD32F2F)
            else -> Color.Red
        },
        label = "swipeBg"
    )
    SwipeToDismissBox(
        modifier = modifier,
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor),
                contentAlignment = Alignment.CenterEnd
            ) {
                Column(
                    modifier = Modifier.padding(end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector= Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "Удалить",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    ) {
        ProductCard(
            product=product,
            onEdit = onEdit,
            isPendingDeletion = isPendingDeletion,
        )
    }
}