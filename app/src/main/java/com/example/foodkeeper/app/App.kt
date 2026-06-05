package com.example.foodkeeper.app

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.foodkeeper.di.authModule
import com.example.foodkeeper.di.firebaseModule
import com.example.foodkeeper.di.firestoreModule
import com.example.foodkeeper.di.repositoryModule
import com.example.foodkeeper.di.roomModule
import com.example.foodkeeper.di.useCaseModule
import com.example.foodkeeper.di.viewModelModule
import com.example.foodkeeper.data.local.fb.SyncWorker
import com.example.foodkeeper.di.settingsModule
import com.example.foodkeeper.presentation.notifications.ExpiryCheckWorker
import com.example.foodkeeper.presentation.notifications.ExpiryNotificationManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit


class App : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@App)
            modules(listOf(
                roomModule,
                repositoryModule,
                viewModelModule,
                useCaseModule,
                authModule,
                firebaseModule,
                firestoreModule,
                settingsModule
            ))
        }

        ExpiryNotificationManager.createNotificationChannel(this)

        scheduleExpiryCheck()
        scheduleFirestoreSync()
        //val testRequest = OneTimeWorkRequestBuilder<ExpiryCheckWorker>().build()
        //WorkManager.getInstance(this).enqueue(testRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleExpiryCheck() {
        val expiryCheckRequest = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setInitialDelay(calculateDelayUntil8am(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "expiry_check",
            ExistingPeriodicWorkPolicy.KEEP,
            expiryCheckRequest
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateDelayUntil8am(): Long {
        val now = LocalDateTime.now()
        var target = now.withHour(8).withMinute(0).withSecond(0)
        if (now.isAfter(target)) target = target.plusDays(1)
        return ChronoUnit.MILLIS.between(now, target)
    }

    private fun scheduleFirestoreSync() {
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            repeatInterval = 30,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "firestore_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}