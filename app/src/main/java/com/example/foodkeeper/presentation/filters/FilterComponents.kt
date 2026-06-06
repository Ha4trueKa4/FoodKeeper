package com.example.foodkeeper.presentation.filters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.foodkeeper.domain.Category
import com.example.foodkeeper.domain.StorageLocation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filter: ProductFilter,
    onFilterChange: (ProductFilter) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Фильтры", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            // Категория
            FilterSectionHeader("Категория")
            SingleChoiceChipRow(
                options = listOf(null) + Category.entries,
                selected = filter.category,
                label = { it?.let { "${it.emoji} ${it.displayName}" } ?: "Все" },
                onSelect = { onFilterChange(filter.copy(category = it)) }
            )

            // Место хранения
            FilterSectionHeader("Место хранения")
            SingleChoiceChipRow(
                options = listOf(null) + StorageLocation.entries,
                selected = filter.storageLocation,
                label = { it?.let { "${it.emoji} ${it.displayName}" } ?: "Все" },
                onSelect = { onFilterChange(filter.copy(storageLocation = it)) }
            )

            // Сортировка
            FilterSectionHeader("Сортировка")
            SingleChoiceChipRow(
                options = SortBy.entries,
                selected = filter.sortBy,
                label = { it.displayName },
                onSelect = { onFilterChange(filter.copy(sortBy = it)) }
            )

            // Показывать истёкшие
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Показывать истёкшие", style = MaterialTheme.typography.bodyMedium)
                Switch(
                    checked = filter.showExpired,
                    onCheckedChange = { onFilterChange(filter.copy(showExpired = it)) }
                )
            }

            // Сброс
            if (filter.isActive) {
                OutlinedButton(
                    onClick = { onReset() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Сбросить фильтры")
                }
            }
        }
    }
}

@Composable
private fun FilterSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun <T> SingleChoiceChipRow(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(options.size) { index ->
            val option = options[index]
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(label(option)) }
            )
        }
    }
}