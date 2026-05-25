package com.example.foodkeeper.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyProductsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.AddShoppingCart,
            contentDescription = "Пусто",
            modifier = Modifier.size(88.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )

        Text(
            text = "Ваш холодильник пуст",
            fontSize = 20.sp,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = "Добавьте первый продукт, нажав кнопку \"+\" внизу справа",
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

