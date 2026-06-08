package com.example.foodkeeper.presentation.filters

import com.example.foodkeeper.domain.Category
import com.example.foodkeeper.domain.StorageLocation

enum class SortBy(val displayName: String) {
    EXPIRY_DATE("По сроку"),
    NAME("По названию"),
    CATEGORY("По категории")
}

data class ProductFilter(
    val category: Category? = null,
    val storageLocation: StorageLocation? = null,
    val showExpired: Boolean = true,
    val sortBy: SortBy = SortBy.EXPIRY_DATE
) {
    val isActive: Boolean
        get() = category != null || storageLocation != null || !showExpired || sortBy != SortBy.EXPIRY_DATE
}