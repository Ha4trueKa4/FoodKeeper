package com.example.foodkeeper.domain.usecases

import com.example.foodkeeper.data.local.ProductRepositoryImpl

class SyncUseCase(private val repository: ProductRepositoryImpl) {
    suspend fun execute() = repository.syncFromFirestore()
}