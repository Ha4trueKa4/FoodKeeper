package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend fun execute(productId: Int): Product? {
        return repository.getProductById(productId)
    }
}