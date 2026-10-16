package com.example.namazvakti.core.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.namazvakti.R
import com.example.namazvakti.ui.alarm.AlarmActivity

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Namaz"
        val prayerTime = intent.getStringExtra("PRAYER_TIME") ?: "--:--"
        val nextPrayerTime = intent.getStringExtra("NEXT_PRAYER_TIME") ?: "--:--"
        val cityName = intent.getStringExtra("CITY_NAME") ?: "Mersin"

        showNotificationWithFullScreenIntent(context, cityName, prayerName, prayerTime, nextPrayerTime)
    }

    private fun showNotificationWithFullScreenIntent(
        context: Context,
        cityName: String,
        prayerName: String,
        prayerTime: String,
        nextPrayerTime: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "prayer_alarm_channel_v2"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Namaz Vakti Alarmları",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Namaz vakti geldiğinde tam ekran uyarı verir."
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Tam Ekran Activity Intent'i
        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
            putExtra("CITY_NAME", cityName)
            putExtra("PRAYER_NAME", prayerName)
            putExtra("PRAYER_TIME", prayerTime)
            putExtra("NEXT_PRAYER_TIME", nextPrayerTime)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$cityName — $prayerName Vakti")
            .setContentText("$prayerName ezanı okunuyor ($prayerTime)")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}