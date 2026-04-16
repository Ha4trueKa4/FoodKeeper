package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository : ProductRepository) {
    fun execute() : Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}