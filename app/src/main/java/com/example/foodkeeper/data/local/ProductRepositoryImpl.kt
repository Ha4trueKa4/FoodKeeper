package com.example.foodkeeper.data.local

import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class ProductRepositoryImpl(private val productDao: ProductDao) : ProductRepository {
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map{
                Product(id = it.id, name = it.name, expiryDate = it.expiryDate , imageUrl = it.imageUrl)
            }
        }
    }

    override suspend fun addProduct(product: Product) {
        val productEntity = ProductEntity(
            id = product.id,
            name = product.name,
            expiryDate = product.expiryDate,
            imageUrl = product.imageUrl
        )
        productDao.insert(productEntity)
    }

    override suspend fun deleteProduct(productId: Int) {
        productDao.deleteProduct(productId)
    }


}