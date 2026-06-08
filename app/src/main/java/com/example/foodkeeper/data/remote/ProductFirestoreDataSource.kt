package com.example.foodkeeper.data.remote

import com.example.foodkeeper.domain.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await

class ProductFirestoreDataSource(
    private val firestore: FirebaseFirestore
) {

    private fun getUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid 
            ?: throw IllegalStateException("User not authenticated")
    }

    private fun getUserProductsCollection() = 
        firestore.collection("users").document(getUserId()).collection("products")

    suspend fun addProduct(product: Product) {
        try {
            val userId = getUserId()
            val productWithUserId = product.copy(userId = userId)
            val documentId = product.firebaseId
            
            getUserProductsCollection().document(documentId)
                .set(productWithUserId)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun deleteProduct(firebaseId: String) {
        try {
            getUserProductsCollection().document(firebaseId)
                .delete()
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateProduct(product: Product) {
        try {
            val userId = getUserId()
            val productWithUserId = product.copy(userId = userId)
            val documentId = product.firebaseId
            
            getUserProductsCollection().document(documentId)
                .set(productWithUserId)
                .await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getAllProducts(): List<Product> {
        try {
            val querySnapshot = getUserProductsCollection().get(Source.SERVER).await()
            
            val products = querySnapshot.documents.mapNotNull { document ->
                val product = document.toObject(Product::class.java)
                product?.copy(firebaseId = document.id)
            }
            
            return products
        } catch (e: Exception) {
            throw e
        }
    }
}