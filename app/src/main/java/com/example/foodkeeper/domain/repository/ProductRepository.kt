
package com.example.foodkeeper.domain.repository

import com.example.foodkeeper.domain.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(firebaseId: String)

    suspend fun getProductById(firebaseId: String) : Product?

    suspend fun updateProduct(product: Product)
}