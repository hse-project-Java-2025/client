package org.hse.smartcalendar.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import org.hse.smartcalendar.network.ApiClient
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.repository.StatisticsRepository

class StatisticsUploadWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    val repo = StatisticsRepository(ApiClient.statisticsApiService)
    override suspend fun doWork(): Result {
        val json = inputData.getString("statistics_json") ?: return Result.failure()
        val stats = Json.decodeFromString<StatisticsDTO>(json)

        val success = try {
            repo.updateStatistics(stats)
            true
        } catch (e: Exception) {
            false
        }

        return if (success) Result.success() else Result.retry()
    }
}