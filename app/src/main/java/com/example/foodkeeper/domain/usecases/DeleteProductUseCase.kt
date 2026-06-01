package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository

class DeleteProductUseCase(private val productRepository : ProductRepository) {
    suspend fun execute(firebaseId : String){
        return productRepository.deleteProduct(firebaseId)
    }
}