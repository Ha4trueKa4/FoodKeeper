package com.example.foodkeeper.data.local.mapper

import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.domain.Category
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.StorageLocation
import com.example.foodkeeper.domain.Units


fun ProductEntity.toDomain(): Product {
    return Product(
        firebaseId = firebaseId,
        name = name,
        expiryDate = expiryDate,
        imageUrl = imageUrl,
        userId = userId,
        category = Category.valueOf(category),
        quantity = quantity,
        unit = Units.valueOf(unit),
        openedDate = openedDate,
        storageLocation = StorageLocation.valueOf(storageLocation),
        notes = notes
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        firebaseId = firebaseId,
        name = name,
        expiryDate = expiryDate,
        imageUrl = imageUrl,
        userId = userId,
        category = category.name,
        quantity = quantity,
        unit = unit.name,
        openedDate = openedDate,
        storageLocation = storageLocation.name,
        notes = notes
    )
}