package com.example.foodkeeper.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import coil3.compose.AsyncImage
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodkeeper.R
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.presentation.theme.Dimens
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ProductCard(
    product: Product,
    modifier: Modifier = Modifier,
    onEdit: (String) -> Unit,
    isPendingDeletion: Boolean = false
) {
    val daysLeft = calculateDaysLeft(product.expiryDate)

    val statusColor = when {
        daysLeft < 0 -> Color(0xFFD32F2F)
        daysLeft <= 3 -> Color(0xFFFFA726)
        daysLeft <= 7 -> Color(0xFFFDD835)
        else -> Color(0xFF66BB6A)
    }

    val statusText = when {
        daysLeft < 0 -> "Истекло"
        daysLeft == 0 -> "Истекает сегодня"
        daysLeft == 1 -> "Остался 1 день"
        else -> "Осталось $daysLeft дней"
    }

    val animatedColor by animateColorAsState(targetValue = statusColor, label = "statusColor")
    val cardAlpha = if (isPendingDeletion) 0.6f else 1f

    Card(
        modifier = modifier
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingMedium)
            .fillMaxWidth()
            .height(130.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.alpha(cardAlpha)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Изображение
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFF5F5F5))
                        .then(
                            if (isPendingDeletion) Modifier.border(
                                width = 2.dp,
                                color = Color.Red
                            ) else Modifier
                        )
                ) {
                    if (product.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.milk),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
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
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        InfoChip("${product.category.emoji} ${product.category.displayName}")
                        InfoChip("${product.storageLocation.emoji} ${product.storageLocation.displayName}")
                        InfoChip("${formatQuantity(product.quantity)} ${product.unit.displayName}")
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
private fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            maxLines = 1
        )
    }
}
private fun formatQuantity(quantity: Float): String =
    if (quantity % 1f == 0f) quantity.toInt().toString() else quantity.toString()
private fun calculateDaysLeft(expiryDateMillis: Long): Int {
    val today = Date()
    val expiryDate = Date(expiryDateMillis)
    val diffMillis = expiryDate.time - today.time
    return (diffMillis / (1000 * 60 * 60 * 24)).toInt()
}

private fun formatDate(timeMillis: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.forLanguageTag("ru-RU"))
    return dateFormat.format(Date(timeMillis))
}

