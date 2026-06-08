package com.example.foodkeeper.di


import androidx.room.Room
import com.example.foodkeeper.data.local.FoodKeeperDatabase
import com.example.foodkeeper.data.remote.ImageStorageDataSource
import com.example.foodkeeper.data.remote.ProductFirestoreDataSource
import com.example.foodkeeper.data.repository.ProductRepositoryImpl
import com.example.foodkeeper.data.repository.SettingsRepository
import com.example.foodkeeper.domain.repository.ProductRepository
import com.example.foodkeeper.domain.usecases.AddProductUseCase
import com.example.foodkeeper.domain.usecases.DeleteProductUseCase
import com.example.foodkeeper.domain.usecases.GetProductByIdUseCase
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import com.example.foodkeeper.domain.usecases.SyncUseCase
import com.example.foodkeeper.domain.usecases.UpdateProductUseCase
import com.example.foodkeeper.presentation.viewmodel.AuthViewModel
import com.example.foodkeeper.presentation.viewmodel.FoodKeeperViewModel
import com.example.foodkeeper.presentation.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel {
        FoodKeeperViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
    viewModel {
        AuthViewModel(get(), get())
    }
}

val roomModule = module {
    single {
        Room.databaseBuilder(get(), FoodKeeperDatabase::class.java, "food_keeper_database")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<FoodKeeperDatabase>().getProductDao() }
}

val repositoryModule = module {

    single<ProductRepositoryImpl> {
        ProductRepositoryImpl(
            get(),
            get(),
            get()
        )
    }
    
    single<ProductRepository> {
        get<ProductRepositoryImpl>()
    }
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
    single {
        UpdateProductUseCase(get())
    }
    single {
        GetProductByIdUseCase(get())
    }
    single {
        SyncUseCase(get())
    }
}

val authModule = module {
    single {
        FirebaseAuth.getInstance()
    }
}

val firebaseModule = module {

    single {
        com.google.firebase.firestore.FirebaseFirestore.getInstance()
    }

    single {
        FirebaseAuth.getInstance()
    }
}

val firestoreModule = module {
    single { ProductFirestoreDataSource(get()) }
    single { ImageStorageDataSource() }
}

val settingsModule = module {
    single { SettingsRepository(get()) }
    viewModel { SettingsViewModel(get(), androidApplication()) }
}