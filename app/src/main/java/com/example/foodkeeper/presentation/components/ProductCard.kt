package com.example.foodkeeper.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.R
import com.example.foodkeeper.domain.Product

import com.example.foodkeeper.presentation.theme.Dimens


@Composable
fun ProductCard(product : Product, modifier: Modifier = Modifier, onDelete : (Product) -> Unit) {
    val daysLeft = 10
    val statusColor = Color.Green

    Row(
        modifier = modifier
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingMedium)
            .fillMaxWidth()
            .height(100.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(Dimens.PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.milk),
            contentDescription = null,
            modifier = Modifier
                .padding(Dimens.PaddingMedium)
                .size(60.dp)
                .clip(RoundedCornerShape(100)),
            contentScale = ContentScale.Crop

        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

                )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = Dimens.PaddingMedium, vertical = Dimens.PaddingSmall)
            ) {
                Text(
                    text = "Срок истекает: ${product.expiryDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = Dimens.PaddingSmall)
                )
            }
        }

        IconButton(
            onClick = { onDelete(product) }
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
        }
    }
}
