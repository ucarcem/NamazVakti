package com.example.namazvakti.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.namazvakti.core.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PrayerAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        val prayerName = intent?.getStringExtra("EXTRA_PRAYER_NAME") ?: "Namaz"

        if (context != null) {
            notificationHelper.showPrayerNotification(prayerName)
        }
    }
}