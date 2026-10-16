package com.example.namazvakti

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.namazvakti.core.worker.PrayerSyncScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NamazVaktiApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var prayerSyncScheduler: PrayerSyncScheduler

    override fun onCreate() {
        super.onCreate()
        // Uygulama başlatıldığında periyodik veri senkronizasyonunu ayarla
        prayerSyncScheduler.schedulePeriodicSync()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}