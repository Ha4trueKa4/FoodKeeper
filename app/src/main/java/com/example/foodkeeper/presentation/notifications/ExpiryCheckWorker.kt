package com.example.foodkeeper.presentation.notifications

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExpiryCheckWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val getProductsUseCase: GetProductsUseCase by inject()

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val notifyDays = inputData.getInt("notify_days", 3)

            val products = getProductsUseCase.execute().first()

            checkAndNotify(products, notifyDays)

            Result.success()
        } catch (_: Exception) {
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun checkAndNotify(products: List<Product>, notifyDays: Int) {

        val expired = mutableListOf<Pair<String, Int>>()
        val expiring = mutableListOf<Pair<String, Int>>()

        products.forEach { product ->
            val now = System.currentTimeMillis()
            val daysLeft = ((product.expiryDate - now) / (1000 * 60 * 60 * 24)).toInt()
            Log.d("Worker", "Product: ${product.name}, daysLeft: $daysLeft")
            Log.d("Worker", "notifyDays: $notifyDays, daysLeft: $daysLeft")
            when {
                daysLeft < 0 -> expired.add(Pair(product.name, daysLeft))
                daysLeft <= notifyDays -> expiring.add(Pair(product.name, daysLeft))
            }
        }
        Log.d("Worker", "expired: ${expired.size}, expiring: ${expiring.size}")

        if (expiring.isNotEmpty()) {
            ExpiryNotificationManager.showExpiringNotification(
                applicationContext,
                expiring.map { it.first },
                expiring.size
            )
        }

        if (expired.isNotEmpty()) {
            delay(5000)
            ExpiryNotificationManager.showExpiredNotification(
                applicationContext,
                expired.map { it.first },
                expired.size
            )
        }


    }
}
