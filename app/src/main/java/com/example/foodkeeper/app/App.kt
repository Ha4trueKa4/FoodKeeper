package com.example.foodkeeper.app

import android.app.Application
import com.example.foodkeeper.di.repositoryModule
import com.example.foodkeeper.di.roomModule
import com.example.foodkeeper.di.useCaseModule
import com.example.foodkeeper.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(roomModule, repositoryModule, viewModelModule, useCaseModule))
        }
    }
}