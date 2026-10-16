package com.example.namazvakti.core.alarm

interface AlarmScheduler {
    fun scheduleAlarm(prayerName: String, timeString: String, cityName: String)
    fun cancelAlarm(prayerName: String)
}