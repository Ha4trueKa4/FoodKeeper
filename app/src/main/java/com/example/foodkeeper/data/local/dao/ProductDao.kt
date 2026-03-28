package com.example.foodkeeper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodkeeper.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product : ProductEntity)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts() : List<ProductEntity>

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: Int)

}