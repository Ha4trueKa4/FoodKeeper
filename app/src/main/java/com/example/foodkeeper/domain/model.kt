package com.example.foodkeeper.domain

data class Product(
    val id: Int,
    val name: String,
    val expiryDate: Long,
    val imageUrl: String
)