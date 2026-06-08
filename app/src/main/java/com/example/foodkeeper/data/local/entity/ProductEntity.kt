package com.example.foodkeeper.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.foodkeeper.domain.Category
import com.example.foodkeeper.domain.StorageLocation
import com.example.foodkeeper.domain.Units

@Entity(
    tableName = "products",
    indices = [Index(value = ["firebaseId"], unique = true)]
)
data class ProductEntity(
    @PrimaryKey
    val firebaseId: String,
    val name: String,
    val expiryDate: Long,
    val imageUrl: String,
    val userId: String = "",
    val category: String = Category.OTHER.name,
    val quantity: Float = 1f,
    val unit: String = Units.PCS.name,
    val storageLocation: String = StorageLocation.FRIDGE.name,
    val notes: String = "",
    val isSynced: Boolean = false
)