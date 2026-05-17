package com.example.foodkeeper.domain

data class Product(
    var id: Int = 0,
    var name: String = "",
    var expiryDate: Long = 0L,
    var imageUrl: String = "",
    val userId: String = "",
    val firebaseId: String = "",
)