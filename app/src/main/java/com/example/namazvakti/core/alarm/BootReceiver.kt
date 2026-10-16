package com.example.namazvakti.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.namazvakti.domain.usecase.SchedulePrayerAlarmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var schedulePrayerAlarmsUseCase: SchedulePrayerAlarmsUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // BroadcastReceiver'ın arka planda öldürülmesini önlemek için goAsync() kullanıyoruz
            val pendingResult = goAsync()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    schedulePrayerAlarmsUseCase()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}