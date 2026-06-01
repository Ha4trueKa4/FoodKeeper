package com.example.foodkeeper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.util.UUID

@Entity(
    tableName = "products",
    indices = [Index(value = ["firebaseId"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey
    val firebaseId: String,
    val name: String,
    val expiryDate : Long,
    val imageUrl: String,
    val userId: String = "",

)