package com.example.foodkeeper.domain

import java.util.UUID

data class Product(
    val firebaseId: String = UUID.randomUUID().toString(),
    var name: String = "",
    var expiryDate: Long = 0L,
    var imageUrl: String = "",
    val userId: String = "",
    val category: Category = Category.OTHER,
    val quantity: Float = 1f,
    val unit: Units = Units.PCS,
    val openedDate: Long? = null,
    val storageLocation: StorageLocation = StorageLocation.FRIDGE,
    val notes: String = ""
)