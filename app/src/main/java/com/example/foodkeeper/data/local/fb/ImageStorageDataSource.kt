package com.example.foodkeeper.data.local.fb

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.resume

class ImageStorageDataSource(private val context: Context) {
    companion object {
        private var initialized = false

        fun init(context: Context) {
            if (!initialized) {
                val config = mapOf(
                    "cloud_name" to "dctk3cosu"
                )
                MediaManager.init(context, config)
                initialized = true
            }
        }
    }
    suspend fun uploadImage(uri: Uri): String = suspendCancellableCoroutine { cont ->
        MediaManager.get().upload(uri)
            .unsigned("foodkeeper_preset")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {}
                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {}
                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    if (url != null) cont.resume(url)
                    else cont.resumeWithException(Exception("No URL in response"))
                }
                override fun onError(requestId: String, error: ErrorInfo) {
                    cont.resumeWithException(Exception(error.description))
                }
                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    cont.resumeWithException(Exception(error.description))
                }
            })
            .dispatch()
    }
}