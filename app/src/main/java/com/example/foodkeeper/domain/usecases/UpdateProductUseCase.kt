package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository

class UpdateProductUseCase(
    private val repository: ProductRepository
) {
    suspend fun execute(product: Product) {
        repository.updateProduct(product)
    }
}