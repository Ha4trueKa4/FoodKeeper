package com.example.foodkeeper.domain

data class Product(
    val id: Int,
    val name: String,
    val expiryDate: String,
    val imageUrl: String
)