package com.example.foodkeeper.presentation.notifications

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.foodkeeper.domain.Product
import com.example.foodkeeper.domain.usecases.GetProductsUseCase
import kotlinx.coroutines.flow.first
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val products = getProductsUseCase.execute().first()
            checkAndNotify(products)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAndNotify(products: List<Product>) {
        val today = LocalDate.now()

        val expiring = products.mapNotNull { product ->
            val expiryDate = Instant.ofEpochMilli(product.expiryDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val daysLeft = ChronoUnit.DAYS.between(today, expiryDate).toInt()
            if (daysLeft <= 3) Pair(product.name, daysLeft) else null
        }

        when {
            expiring.isEmpty() -> return

            expiring.size == 1 -> {
                val (name, daysLeft) = expiring.first()
                ExpiryNotificationManager.showExpiryNotification(
                    applicationContext, name, daysLeft
                )
            }

            else -> {
                ExpiryNotificationManager.showMultipleExpiryNotification(
                    applicationContext,
                    expiring.map { it.first },
                    expiring.size
                )
            }
        }
    }
}
