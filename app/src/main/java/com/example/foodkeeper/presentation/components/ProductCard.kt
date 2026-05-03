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
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    isPendingDeletion : Boolean = false
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

    val cardBackground = if (isPendingDeletion) {
        Color(0xFFFFEBEE)
    } else {
        MaterialTheme.colorScheme.surface
    }

    val cardAlpha = if (isPendingDeletion) 0.6f else 1f

    Card(
        modifier = modifier
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingMedium)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .alpha(cardAlpha)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF5F5F5))
                        .border(
                            width = if (isPendingDeletion) 2.dp else 0.dp,
                            color = if (isPendingDeletion) Color.Red else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    if (product.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.milk),
                            contentDescription = null,
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = if (isPendingDeletion) {
                            Color.Red
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isPendingDeletion) {
                                Color.Red.copy(alpha = 0.2f)
                            } else {
                                animatedColor.copy(alpha = 0.15f)
                            })
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isPendingDeletion) {
                                "⏳ Удаляется..."
                            } else {
                                statusText
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isPendingDeletion) Color.Red else animatedColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Дата: ${formatDate(product.expiryDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = if (isPendingDeletion) 0.05f else 0.1f
                            )
                        )
                        .clickable(
                            enabled = !isPendingDeletion,
                            onClick = { onEdit(product.id) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Редактировать",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Редактировать",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isPendingDeletion) {
                                Color.Red.copy(alpha = 0.3f)
                            } else {
                                Color.Red.copy(alpha = 0.1f)
                            }
                        )
                        .clickable(
                            enabled = !isPendingDeletion,
                            onClick = { onDelete(product.id) }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Transparent)
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Удалить",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Red,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

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

