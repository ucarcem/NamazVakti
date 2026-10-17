package com.example.namazvakti.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.namazvakti.core.notification.NotificationHelper
import com.example.namazvakti.ui.alarm.AlarmActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrayerAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Namaz"
        val prayerTime = intent.getStringExtra("PRAYER_TIME") ?: ""
        val cityName = intent.getStringExtra("CITY_NAME") ?: ""

        // Sistem bildirimi göster ve fullScreenIntent tetikle
        notificationHelper.showPrayerNotification(prayerName, prayerTime, cityName)

        // Arka plandan aktivite başlatma denemesi (Eski cihazlar / Destekleyen ROM'lar için yedek)
        try {
            val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("PRAYER_NAME", prayerName)
                putExtra("PRAYER_TIME", prayerTime)
                putExtra("CITY_NAME", cityName)
            }
            context.startActivity(alarmIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}