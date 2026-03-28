package com.example.foodkeeper.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodkeeper.data.local.FoodKeeperDatabase
import com.example.foodkeeper.data.local.ProductRepositoryImpl
import com.example.foodkeeper.domain.usecases.AddProductUseCase
import com.example.foodkeeper.domain.usecases.DeleteProductUseCase
import com.example.foodkeeper.domain.usecases.GetProductsUseCase

class FoodKeeperViewModelFactory(
    private val context: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FoodKeeperViewModel::class.java)) {

            val db = FoodKeeperDatabase.getDataBase(context)
            val dao = db.getProductDao()

            val repository = ProductRepositoryImpl(dao)

            val getProductsUseCase = GetProductsUseCase(repository)
            val addProductUseCase = AddProductUseCase(repository)
            val deleteProductUseCase = DeleteProductUseCase(repository)

            @Suppress("UNCHECKED_CAST")
            return FoodKeeperViewModel(getProductsUseCase, addProductUseCase, deleteProductUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}