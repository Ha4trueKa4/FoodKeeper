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
            val notifyDays = inputData.getInt("notify_days", 3)
            val products = getProductsUseCase.execute().first()
            checkAndNotify(products, notifyDays)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAndNotify(products: List<Product>, notifyDays: Int) {
        val today = LocalDate.now()

        val expired = mutableListOf<Pair<String, Int>>()
        val expiring = mutableListOf<Pair<String, Int>>()

        products.forEach { product ->
            val expiryDate = Instant.ofEpochMilli(product.expiryDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val daysLeft = ChronoUnit.DAYS.between(today, expiryDate).toInt()
            when {
                daysLeft < 0 -> expired.add(Pair(product.name, daysLeft))
                daysLeft <= notifyDays -> expiring.add(Pair(product.name, daysLeft))
            }
        }

        if (expired.isNotEmpty()) {
            ExpiryNotificationManager.showExpiredNotification(
                applicationContext,
                expired.map { it.first },
                expired.size
            )
        }

        if (expiring.isNotEmpty()) {
            ExpiryNotificationManager.showExpiringNotification(
                applicationContext,
                expiring.map { it.first },
                expiring.size
            )
        }
    }
}
