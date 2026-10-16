package com.example.namazvakti.core.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.namazvakti.ui.alarm.AlarmActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Namaz"
        val prayerTime = intent.getStringExtra("PRAYER_TIME") ?: ""
        val cityName = intent.getStringExtra("CITY_NAME") ?: ""

        val alarmIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("PRAYER_NAME", prayerName)
            putExtra("PRAYER_TIME", prayerTime)
            putExtra("CITY_NAME", cityName)
        }
        context.startActivity(alarmIntent)
    }
}