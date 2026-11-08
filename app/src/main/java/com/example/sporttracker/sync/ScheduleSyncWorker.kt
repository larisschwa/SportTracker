package com.example.sporttracker.sync

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.sporttracker.data.local.AppDatabase
import com.example.sporttracker.data.repository.SportsRepository
import com.example.sporttracker.widget.NextGameWidget
import java.util.concurrent.TimeUnit

class ScheduleSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("ScheduleSyncWorker", "Starting daily schedule background sync on Wi-Fi...")
            val db = AppDatabase.getInstance(applicationContext)
            val repository = SportsRepository(applicationContext, db.teamDao(), db.gameDao())

            val message = repository.refreshSchedules(force = true)
            Log.d("ScheduleSyncWorker", "Background sync completed: $message")

            try {
                NextGameWidget().updateAll(applicationContext)
            } catch (e: Exception) {
                Log.w("ScheduleSyncWorker", "Failed to update widget: ${e.message}")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("ScheduleSyncWorker", "Error in daily background sync: ${e.message}", e)
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "daily_schedule_sync_wifi"

        fun enqueueDailySync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi / unmetered connection only
                .build()

            val syncRequest = PeriodicWorkRequestBuilder<ScheduleSyncWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
            Log.d("ScheduleSyncWorker", "Enqueued daily background sync (Wi-Fi only, 1 day interval).")
        }
    }
}
