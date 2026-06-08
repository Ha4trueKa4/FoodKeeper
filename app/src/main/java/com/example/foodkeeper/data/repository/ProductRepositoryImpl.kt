package com.example.foodkeeper.data.repository

import android.util.Log
import androidx.core.net.toUri
import com.example.foodkeeper.data.local.dao.ProductDao
import com.example.foodkeeper.data.local.mapper.toDomain
import com.example.foodkeeper.data.local.mapper.toEntity
import com.example.foodkeeper.data.remote.ImageStorageDataSource
import com.example.foodkeeper.data.remote.ProductFirestoreDataSource
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val firestore: ProductFirestoreDataSource,
    private val imageStorage: ImageStorageDataSource
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addProduct(product: Product) {
        productDao.insert(product.copy(isSynced = false).toEntity())
        Log.d("Sync", "Inserted locally, isSynced=false")
        try {
            firestore.addProduct(product)
            productDao.updateSyncStatus(product.firebaseId, true)
            Log.d("Sync", "Firestore success, isSynced=true")
        } catch (e: Exception) {
            Log.d("Sync", "Firestore failed: ${e.message}")
        }
    }

    override suspend fun deleteProduct(firebaseId: String) {
        productDao.deleteProduct(firebaseId)
        try {
            if (firebaseId.isNotBlank()) firestore.deleteProduct(firebaseId)
        } catch (_: Exception) { }
    }

    override suspend fun getProductById(firebaseId: String): Product? {
        return productDao.getProductById(firebaseId)?.toDomain()
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product.copy(isSynced = false).toEntity())
        try {
            firestore.updateProduct(product)
            productDao.updateProduct(product.copy(isSynced = true).toEntity())
        } catch (_: Exception) { }
    }

    suspend fun syncFromFirestore() {
        try {
            val remoteProducts = firestore.getAllProducts()
            val remoteIds = remoteProducts.map { it.firebaseId }.toSet()
            val localProducts = productDao.getAllProducts().first()
            val localMap = localProducts.associateBy { it.firebaseId }

            remoteProducts.forEach { remote ->
                val local = localMap[remote.firebaseId]
                val remoteEntity = remote.copy(isSynced = true).toEntity()

                if (local == null || local != remoteEntity) {
                    productDao.insertOrReplace(remoteEntity)
                } else if (!local.isSynced) {
                    productDao.updateSyncStatus(local.firebaseId, true)
                }
            }

            localProducts.forEach { local ->
                if (local.firebaseId !in remoteIds) {
                    if (local.isSynced) {
                        productDao.deleteProduct(local.firebaseId)
                    } else {
                        try {
                            val product = local.toDomain()

                            val finalImageUrl = if (
                                product.imageUrl.isNotBlank() &&
                                !product.imageUrl.startsWith("https://")
                            ) {
                                try {
                                    imageStorage.uploadImage(product.imageUrl.toUri())
                                } catch (_: Exception) {
                                    product.imageUrl
                                }
                            } else {
                                product.imageUrl
                            }

                            val productToSync = product.copy(imageUrl = finalImageUrl)
                            firestore.addProduct(productToSync)

                            productDao.insertOrReplace(productToSync.copy(isSynced = true).toEntity())
                        } catch (e: Exception) {
                            Log.d("Sync", "Failed to sync product: ${e.message}")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("Sync", "Sync failed: ${e.message}")
        }
    }

    suspend fun clearAllProducts() {
        productDao.clearAll()
    }
}