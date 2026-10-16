package com.example.namazvakti.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.namazvakti.core.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrayerAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val prayerName = intent?.getStringExtra("EXTRA_PRAYER_NAME") ?: "Namaz"
            Log.d("PrayerAlarmReceiver", "Alarm tetiklendi: $prayerName")

            if (context != null) {
                notificationHelper.showPrayerNotification(prayerName)
            }
        } catch (e: Exception) {
            Log.e("PrayerAlarmReceiver", "Bildirim gösterilirken hata oluştu: ${e.localizedMessage}", e)
        }
    }
}