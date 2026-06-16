package com.zeeb.luna

import android.app.Application
import androidx.work.*
import com.zeeb.luna.worker.MediaSyncWorker
import com.zeeb.luna.worker.TrashCleanupWorker
import java.util.concurrent.TimeUnit

class LunaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupBackgroundWorkers()
    }

    private fun setupBackgroundWorkers() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<MediaSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        val cleanupRequest = PeriodicWorkRequestBuilder<TrashCleanupWorker>(1, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "MediaSync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "TrashCleanup",
            ExistingPeriodicWorkPolicy.KEEP,
            cleanupRequest
        )
    }
}
