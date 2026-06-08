package com.example.foodkeeper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.example.foodkeeper.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(product: ProductEntity)

    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("DELETE FROM products WHERE firebaseId = :firebaseId")
    suspend fun deleteProduct(firebaseId: String)

    @Query("SELECT * FROM products WHERE firebaseId = :firebaseId LIMIT 1")
    suspend fun getProductById(firebaseId: String): ProductEntity?

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products")
    suspend fun clearAll()

    @Query("UPDATE products SET isSynced = :isSynced WHERE firebaseId = :firebaseId")
    suspend fun updateSyncStatus(firebaseId: String, isSynced: Boolean)
}