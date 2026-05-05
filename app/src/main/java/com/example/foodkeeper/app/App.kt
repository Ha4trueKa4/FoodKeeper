package com.example.foodkeeper.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.foodkeeper.di.repositoryModule
import com.example.foodkeeper.di.roomModule
import com.example.foodkeeper.di.useCaseModule
import com.example.foodkeeper.di.viewModelModule
import com.example.foodkeeper.presentation.notifications.ExpiryCheckWorker
import com.example.foodkeeper.presentation.notifications.ExpiryNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import java.util.concurrent.TimeUnit


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(roomModule, repositoryModule, viewModelModule, useCaseModule))
        }

        ExpiryNotificationManager.createNotificationChannel(this)

        scheduleExpiryCheck()
    }

    private fun scheduleExpiryCheck() {
        val expiryCheckRequest = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "expiery_check",
            ExistingPeriodicWorkPolicy.REPLACE,
            expiryCheckRequest
        )
    }
}