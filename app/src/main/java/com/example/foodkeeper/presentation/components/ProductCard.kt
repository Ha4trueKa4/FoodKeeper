package com.example.foodkeeper.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.presentation.model.Product


@Composable
fun ProductCard(product : Product) {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 40.dp)
            .height(100.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp)) // тень
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(20.dp)) // border
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Срок истекает: ${product.expiryDate}",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductCardPreview() {
    ProductCard(
        product = Product(
            id = 1,
            name = "Молоко Простоквашино",
            expiryDate = "12.11.2008",
            imageUrl = "https://google.com"
        )
    )
}