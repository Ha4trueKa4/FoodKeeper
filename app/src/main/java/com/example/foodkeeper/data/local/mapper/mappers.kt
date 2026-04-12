package com.example.foodkeeper.data.local.mapper

import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.domain.Product


fun ProductEntity.toDomain(): Product {
    return Product(
        id = id,
        name = name,
        expiryDate = expiryDate,
        imageUrl = imageUrl
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id,
        name = name,
        expiryDate = expiryDate,
        imageUrl = imageUrl
    )
}