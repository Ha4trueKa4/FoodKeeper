
package com.example.foodkeeper.domain.repository

import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.domain.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun addProduct(product: Product)
    suspend fun deleteProduct(productId: Int)
}