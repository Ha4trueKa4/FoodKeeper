package com.example.foodkeeper.domain

import java.util.UUID

data class Product(
    val firebaseId: String = UUID.randomUUID().toString(),
    var name: String = "",
    var expiryDate: Long = 0L,
    var imageUrl: String = "",
    val userId: String = "",
)