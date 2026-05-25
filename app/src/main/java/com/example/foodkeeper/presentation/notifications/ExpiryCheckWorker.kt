package com.example.foodkeeper.presentation.notifications

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ExpiryCheckWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val getProductsUseCase: GetProductsUseCase by inject()
    private val notificationManager = ExpiryNotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {

            getProductsUseCase.execute().collect { products ->
                if (!products.isEmpty()) {
                    checkAndNotifyExpiringProducts(products)
                }
            }
            Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAndNotifyExpiringProducts(products: List<Product>) {
        val today = LocalDate.now()

        val expiringProducts = mutableListOf<String>()

        products.forEach { product ->

            val expiryDateLocal = Instant
                .ofEpochMilli(product.expiryDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            val daysLeft = ChronoUnit.DAYS.between(today, expiryDateLocal).toInt()

            if (daysLeft <= 3) {
                notificationManager.showExpiryNotification(
                    applicationContext,
                    productName = product.name,
                    daysLeft = daysLeft
                )
                expiringProducts.add(product.name)
            }
        }

        if (expiringProducts.size > 1) {
            notificationManager.showMultipleExpiryNotification(
                applicationContext,
                expiringProducts,
                expiringProducts.size
            )
        }
    }
}
