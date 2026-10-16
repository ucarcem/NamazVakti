package com.example.namazvakti.core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.namazvakti.core.widget.PrayerWidgetUpdater
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.data.repository.PrayerTimeRepository
import com.example.namazvakti.domain.usecase.SchedulePrayerAlarmsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class PrayerSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val prayerTimeRepository: PrayerTimeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val schedulePrayerAlarmsUseCase: SchedulePrayerAlarmsUseCase,
    private val prayerWidgetUpdater: PrayerWidgetUpdater
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val settings = userPreferencesRepository.userSettingsFlow.first()
            val city = settings.selectedCity

            if (city.isNotBlank()) {
                // Şehrin güncel ay vakitlerini indirip Room DB'ye kaydet
                prayerTimeRepository.fetchAndSavePrayerTimesForMonth(city)

                // Gelecek alarmları ve ana ekran widget'larını yenile
                schedulePrayerAlarmsUseCase()
                prayerWidgetUpdater.updateAllWidgets()
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}