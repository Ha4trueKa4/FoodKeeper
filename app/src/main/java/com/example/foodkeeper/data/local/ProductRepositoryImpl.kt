package com.example.foodkeeper.data.local

import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.entity.ProductEntity
import com.example.foodkeeper.data.local.fb.ProductFirestoreDataSource
import com.example.foodkeeper.data.local.mapper.toDomain
import com.example.foodkeeper.data.local.mapper.toEntity
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID


class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val firestore: ProductFirestoreDataSource
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addProduct(product: Product) {
        productDao.insert(product.toEntity())
        firestore.addProduct(product)
    }

    override suspend fun deleteProduct(firebaseId: String) {
        productDao.deleteProduct(firebaseId)
        if (firebaseId.isNotBlank()) {
            firestore.deleteProduct(firebaseId)
        }
    }

    override suspend fun getProductById(firebaseId: String): Product? {
        return productDao.getProductById(firebaseId)?.toDomain()
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.toEntity())
        firestore.updateProduct(product)
    }

    suspend fun syncFromFirestore() {
        val remoteProducts = firestore.getAllProducts()
        remoteProducts.forEach {
            productDao.insertOrReplace(it.toEntity())
        }
    }

    suspend fun clearAllProducts() {
        productDao.clearAll()
    }
}