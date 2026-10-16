package com.example.namazvakti.core.worker

import android.content.Context
import android.util.Log
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
                Log.d("PrayerSyncWorker", "Arka plan senkronizasyonu başlatıldı: $city")

                // Şehrin güncel ay vakitlerini indir ve DB'ye yaz
                prayerTimeRepository.fetchAndSavePrayerTimesForMonth(city)

                // Gelecek alarmları ve ana ekran widget'larını güncelle
                schedulePrayerAlarmsUseCase()
                prayerWidgetUpdater.updateAllWidgets()

                Log.d("PrayerSyncWorker", "Arka plan senkronizasyonu başarıyla tamamlandı.")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("PrayerSyncWorker", "Arka plan senkronizasyon hatası: ${e.localizedMessage}", e)

            // Eğer 3 defadan fazla hata alındıysa yeniden denemeyi durdur
            if (runAttemptCount > 3) {
                Result.failure()
            } else {
                Result.retry()
            }
        }
    }
}