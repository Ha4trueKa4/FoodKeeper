package com.example.foodkeeper.di


import androidx.room.Room
import com.example.foodkeeper.data.local.FoodKeeperDatabase
import com.example.foodkeeper.data.local.ProductRepositoryImpl
import com.example.foodkeeper.domain.repository.ProductRepository
import com.example.foodkeeper.domain.usecases.AddProductUseCase
import com.example.foodkeeper.domain.usecases.DeleteProductUseCase
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        FoodKeeperViewModel(
            get(),
            get(),
            get()
        )
    }
}

val roomModule = module {
    single {
        Room.databaseBuilder(get(), FoodKeeperDatabase::class.java, "my_database")
            .build()
    }
    single { get<FoodKeeperDatabase>().getProductDao() }
}

val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}

val useCaseModule = module {
    single {
        GetProductsUseCase(get())
    }
    single {
        AddProductUseCase(get())
    }
    single {
        DeleteProductUseCase(get())
    }
}