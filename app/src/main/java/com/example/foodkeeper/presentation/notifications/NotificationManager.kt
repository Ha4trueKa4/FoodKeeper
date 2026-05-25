package com.example.foodkeeper.presentation.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.foodkeeper.MainActivity
import com.example.foodkeeper.R

object ExpiryNotificationManager {
    private const val NOTIFICATION_CHANNEL_ID = "expiry_channel"
    private const val NOTIFICATION_CHANNEL_NAME = "Уведомления об истечении срока годности"
    private const val NOTIFICATION_ID = 1

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Уведомления об истечении сроков продуктов"
                enableVibration(true)
                enableLights(true)
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    fun showExpiryNotification(
        context: Context,
        productName : String,
        daysLeft : Int
    ) {
        val intent = Intent(context, MainActivity::class.java). apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val titleText = when {
            daysLeft < 0 -> "⚠️ Продукт истёк!"
            daysLeft == 0 -> "⚠️ Последний день!"
            daysLeft <= 3 -> "⏰ Скоро испортится!"
            else -> "📢 Проверка сроков"
        }

        val messageText = when {
            daysLeft < 0 -> "$productName истёк ${ Math.abs(daysLeft)} дней назад"
            daysLeft == 0 -> "$productName истекает СЕГОДНЯ!"
            daysLeft == 1 -> "$productName испортится завтра"
            else -> "$productName испортится через $daysLeft дней"
        }

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(titleText)
            .setContentText(messageText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(messageText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setColor(
                when {
                    daysLeft < 0 -> 0xFFD32F2F.toInt()
                    daysLeft <= 3 -> 0xFFFFA726.toInt()
                    else -> 0xFF66BB6A.toInt()
                }
            )
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun showMultipleExpiryNotification(
        context: Context,
        expiringProducts: List<String>,
        totalCount: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val productList = expiringProducts.take(3).joinToString("\n• ")

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("⏰ $totalCount продуктов скоро испортятся!")
            .setContentText("Проверьте список товаров")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Подходящие к концу:\n• ${productList}")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setColor(0xFFFFA726.toInt())
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(NOTIFICATION_ID + 1, notification)
    }
}