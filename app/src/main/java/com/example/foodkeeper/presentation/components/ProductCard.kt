package com.example.foodkeeper.presentation.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.foodkeeper.domain.Category
import com.example.foodkeeper.domain.Product
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit,
    isPendingDeletion: Boolean = false
) {
    val now = System.currentTimeMillis()
    val daysLeft = ((product.expiryDate - now) / (1000 * 60 * 60 * 24)).toInt()

    val statusColor = when {
        daysLeft < 0 -> Color(0xFFD32F2F)
        daysLeft <= 3 -> Color(0xFFFF7314)
        daysLeft <= 7 -> Color(0xFFFFCB00)
        else -> Color(0xFF66BB6A)
    }

    val isLight = MaterialTheme.colorScheme.background.luminance() > 0.5f

    val statusText = when {
        daysLeft < 0 -> "Истекло"
        daysLeft == 0 -> "Истекает сегодня"
        daysLeft % 10 == 1 -> "Остался $daysLeft день"
        daysLeft % 10 in 2..4 -> "Осталось $daysLeft дня"
        else -> "Осталось $daysLeft дней"
    }

    val animatedColor by animateColorAsState(targetValue = statusColor, label = "statusColor")
    val cardAlpha = if (isPendingDeletion) 0.6f else 1f

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(170.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isPendingDeletion -> MaterialTheme.colorScheme.surfaceContainerHigh
                daysLeft < 0 -> if (isLight) Color(0xFFFFEBEE) else Color(0xFF4E1F1F)
                daysLeft <= 3 -> if (isLight) Color(0xFFFFF3E0) else Color(0xFF4E3620)
                daysLeft <= 7 -> if (isLight) Color(0xFFFFFDE7) else Color(0xFF4E4A1F)
                else -> MaterialTheme.colorScheme.surfaceContainerHigh
            }
        )
    ) {
        Column(modifier = Modifier
            .alpha(cardAlpha)
            .fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Изображение
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFF5F5F5))
                ) {
                    if (product.imageUrl.isNotBlank()) {
                        var isLoading by remember { mutableStateOf(true) }

                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            onSuccess = { isLoading = false },
                            onError = { isLoading = false }
                        )

                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = categoryGradient(product.category)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = product.category.emoji,
                                fontSize = 36.sp
                            )
                        }
                    }
                }

                // Правая часть
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Имя + кнопка редактирования
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = if (isPendingDeletion) Color.Red
                            else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        if (!product.isSynced) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = "Не синхронизировано",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(8.dp)
                            )
                        }

                        IconButton(
                            onClick = { onEdit(product.firebaseId) },
                            enabled = !isPendingDeletion,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = if (isPendingDeletion)
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Статус + дата
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isPendingDeletion) Color.Red.copy(alpha = 0.15f)
                                    else animatedColor.copy(alpha = 0.15f)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (isPendingDeletion) "⏳ Удаляется..." else statusText,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isPendingDeletion) Color.Red else animatedColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = formatDate(product.expiryDate),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Чипы
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InfoChip("${product.category.emoji} ${product.category.displayName}")
                            InfoChip("${product.storageLocation.emoji} ${product.storageLocation.displayName}")
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            InfoChip("${formatQuantity(product.quantity)} ${product.unit.displayName}")
                        }
                    }

                    // Заметка
                    Text(
                        text = if (product.notes.isNotBlank()) "📝 ${product.notes}" else "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

}

@Composable
private fun InfoChip(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
private fun formatQuantity(quantity: Float): String =
    if (quantity % 1f == 0f) quantity.toInt().toString() else quantity.toString()

private fun formatDate(timeMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru-RU"))
    return dateFormat.format(Date(timeMillis))
}

private fun categoryGradient(category: Category): List<Color> = when (category) {
    Category.DAIRY -> listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
    Category.MEAT -> listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
    Category.VEGETABLES -> listOf(Color(0xFFE8F5E9), Color(0xFFC8E6C9))
    Category.BAKERY -> listOf(Color(0xFFFFF8E1), Color(0xFFFFECB3))
    Category.CANNED -> listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    Category.DRINKS -> listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2))
    Category.FROZEN -> listOf(Color(0xFFE8EAF6), Color(0xFFC5CAE9))
    Category.OTHER -> listOf(Color(0xFFFAFAFA), Color(0xFFF5F5F5))
    Category.SWEET -> listOf(Color(0xFFFAFAFA), Color(0xFFF5F5F5))
}

