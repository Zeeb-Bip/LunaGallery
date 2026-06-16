package com.zeeb.luna.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeeb.luna.data.repository.MediaRepository

class MediaSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val repository = MediaRepository(applicationContext)
            repository.syncMediaStore()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
