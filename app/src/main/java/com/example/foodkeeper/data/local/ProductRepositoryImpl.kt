package com.example.foodkeeper.data.local

import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.data.local.mapper.toDomain
import com.example.foodkeeper.data.local.mapper.toEntity
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map{
                it.toDomain()
            }
        }
    }

    override suspend fun addProduct(product: Product) {
        productDao.insert(product.toEntity())
    }

    override suspend fun deleteProduct(productId: Int) {
        productDao.deleteProduct(productId)
    }

    override suspend fun getProductById(productId: Int) : Product? {
        return productDao.getProductById(productId)?.toDomain()
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
    }


}