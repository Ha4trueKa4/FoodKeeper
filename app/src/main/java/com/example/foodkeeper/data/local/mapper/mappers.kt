package com.example.foodkeeper.data.local.mapper

import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.domain.Product


fun ProductEntity.toDomain(): Product {
    return Product(
        firebaseId = firebaseId,
        name = name,
        expiryDate = expiryDate,
        imageUrl = imageUrl,
        userId = userId,
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        firebaseId = firebaseId,
        name = name,
        expiryDate = expiryDate,
        imageUrl = imageUrl,
        userId = userId,
    )
}