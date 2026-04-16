package com.example.foodkeeper.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.usecases.AddProductUseCase
import com.example.foodkeeper.domain.usecases.DeleteProductUseCase
import com.example.foodkeeper.domain.usecases.GetProductByIdUseCase
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import com.example.foodkeeper.domain.usecases.UpdateProductUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodKeeperViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductsUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase

) : ViewModel() {
    val products : StateFlow<List<Product>> = getProductsUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _pendingDeleteProduct = MutableStateFlow<Product?>(null)
    val pendingDeleteProduct : StateFlow<Product?> = _pendingDeleteProduct.asStateFlow()

    private var deleteJob : Job? = null

    fun deleteRequest(product: Product) {
        deleteJob?.cancel()
        _pendingDeleteProduct.value = product
        deleteJob = viewModelScope.launch {
            delay(3000)
            finalDeletion()
        }
    }

    private fun finalDeletion() {
        val product = _pendingDeleteProduct.value ?: return

        viewModelScope.launch {
            deleteProductUseCase.execute(product.id)
        }
        deleteJob = null
        _pendingDeleteProduct.value = null
    }

    fun undoDeletion() {
        deleteJob?.cancel()
        deleteJob = null
        _pendingDeleteProduct.value = null
    }



    fun addProductAndAwait(product: Product): Job {
        return viewModelScope.launch {
            addProductsUseCase.execute(product)
        }
    }
    fun deleteProduct(productId: Int) {
        viewModelScope.launch {
            deleteProductUseCase.execute(productId)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            updateProductUseCase.execute(product)
        }
    }

    suspend fun getProductById(productId: Int) : Product? {
        return getProductByIdUseCase.execute(productId)
    }
}