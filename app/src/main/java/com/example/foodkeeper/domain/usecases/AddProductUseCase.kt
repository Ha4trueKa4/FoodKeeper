package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.repository.ProductRepository

class AddProductUseCase (private val productRepository : ProductRepository) {
    suspend fun execute(product: Product){
        return productRepository.addProduct(product)
    }
}