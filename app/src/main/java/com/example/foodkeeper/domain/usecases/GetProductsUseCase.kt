package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository

class GetProductsUseCase(private val productRepository : ProductRepository) {
    suspend fun execute() : List<Product> {
        return productRepository.getAllProducts()
    }
}