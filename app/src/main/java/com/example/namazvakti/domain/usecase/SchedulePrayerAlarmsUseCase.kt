package com.example.namazvakti.domain.usecase

import com.example.namazvakti.core.alarm.AlarmScheduler
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.data.repository.PrayerTimeRepository
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SchedulePrayerAlarmsUseCase @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke() {
        val todayDate = LocalDate.now().toString()
        val prayerTimes = prayerTimeRepository.getPrayerTimesByDate(todayDate) ?: return
        val settings = userPreferencesRepository.userSettingsFlow.first()

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val now = LocalTime.now()

        val prayers = listOf(
            Triple("İmsak", prayerTimes.fajr, settings.fajrAlarmEnabled),
            Triple("Güneş", prayerTimes.sunrise, settings.sunAlarmEnabled),
            Triple("Öğle", prayerTimes.dhuhr, settings.dhuhrAlarmEnabled),
            Triple("İkindi", prayerTimes.asr, settings.asrAlarmEnabled),
            Triple("Akşam", prayerTimes.maghrib, settings.maghribAlarmEnabled),
            Triple("Yatsı", prayerTimes.isha, settings.ishaAlarmEnabled)
        )

        prayers.forEach { (prayerName, timeString, isEnabled) ->
            if (!isEnabled) {
                alarmScheduler.cancelAlarm(prayerName)
                return@forEach
            }

            try {
                val prayerTime = LocalTime.parse(timeString, formatter)
                if (prayerTime.isAfter(now)) {
                    alarmScheduler.scheduleAlarm(prayerName, timeString, settings.selectedCity)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}