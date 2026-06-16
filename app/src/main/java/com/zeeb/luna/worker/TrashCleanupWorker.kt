package com.zeeb.luna.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.zeeb.luna.data.repository.MediaRepository

class TrashCleanupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val repository = MediaRepository(applicationContext)
            val retentionDays = inputData.getInt("retention_days", 30)
            repository.purgeExpiredTrash(retentionDays)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
