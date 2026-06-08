package com.example.foodkeeper.data.remote

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.foodkeeper.data.repository.ProductRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val repository: ProductRepositoryImpl by inject()

    override suspend fun doWork(): Result {
        return try {
            if (FirebaseAuth.getInstance().currentUser == null) {
                return Result.success()
            }

            repository.syncFromFirestore()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}