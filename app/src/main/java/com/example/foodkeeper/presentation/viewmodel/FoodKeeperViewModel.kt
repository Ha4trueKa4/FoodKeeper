package com.example.foodkeeper.presentation.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FoodKeeperViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductsUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductUseCase: UpdateProductUseCase

) : ViewModel() {

    val isLoadingList = MutableStateFlow(true)
    val products : StateFlow<List<Product>> = getProductsUseCase.execute()
        .onEach { isLoadingList.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _pendingDeleteProduct = MutableStateFlow<Product?>(null)
    val pendingDeleteProduct : StateFlow<Product?> = _pendingDeleteProduct.asStateFlow()



    fun requestDelete(product: Product) {
        _pendingDeleteProduct.value?.let { prev ->
            viewModelScope.launch { deleteProductUseCase.execute(prev.firebaseId) }
        }
        _pendingDeleteProduct.value = product
    }

    fun confirmDelete() {
        val product = _pendingDeleteProduct.value ?: return
        _pendingDeleteProduct.value = null
        viewModelScope.launch { deleteProductUseCase.execute(product.firebaseId) }
    }

    fun cancelDelete() {
        _pendingDeleteProduct.value = null
    }



    fun addProductAndAwait(product: Product): Job {
        return viewModelScope.launch {
            addProductsUseCase.execute(product)
        }
    }

    fun deleteProduct(firebaseId : String) {
        viewModelScope.launch {
            deleteProductUseCase.execute(firebaseId)
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            updateProductUseCase.execute(product)
        }
    }

    suspend fun getProductById(firebaseId : String) : Product? {
        return getProductByIdUseCase.execute(firebaseId)
    }
}