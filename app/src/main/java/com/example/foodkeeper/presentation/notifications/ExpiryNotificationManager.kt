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

    fun showExpiredNotification(
        context: Context,
        products: List<String>,
        totalCount: Int
    ) {
        val shown = products.take(3)
        val suffix = if (totalCount > 3) "\n• и ещё ${totalCount - 3}..." else ""
        val productList = shown.joinToString("\n• ")

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(when (totalCount) {
                1->"⚠️ $totalCount продукт истёк!"
                in 2..4->"⚠️ $totalCount продукта истекло!"
                else ->"⚠️ $totalCount продуктов истекло!"
            })
            .setContentText("Проверьте и выбросьте просроченное")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Истекли:\n• $productList$suffix")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(makePendingIntent(context))
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setColor(0xFFD32F2F.toInt())
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, notification)
    }

    fun showExpiringNotification(
        context: Context,
        products: List<String>,
        totalCount: Int
    ) {
        val shown = products.take(3)
        val suffix = if (totalCount > 3) "\n• и ещё ${totalCount - 3}..." else ""
        val productList = shown.joinToString("\n• ")

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(when (totalCount) {
                1->"⏰ $totalCount продукт скоро испортятся!"
                in 2..4->"⏰ $totalCount продукта скоро испортятся!"
                else ->"⏰ $totalCount продуктов скоро испортятся!"
            })
            .setContentText("Проверьте список товаров")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Подходящие к концу:\n• $productList$suffix")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(makePendingIntent(context))
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setColor(0xFFFFA726.toInt())
            .build()

        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID + 1, notification)
    }

    private fun makePendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}