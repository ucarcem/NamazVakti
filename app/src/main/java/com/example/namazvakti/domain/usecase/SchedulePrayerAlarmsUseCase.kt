package com.example.namazvakti.domain.usecase

import com.example.namazvakti.core.alarm.AlarmScheduler
import com.example.namazvakti.data.repository.PrayerTimeRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SchedulePrayerAlarmsUseCase @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke() {
        val today = LocalDate.now()
        val now = LocalDateTime.now()

        // Önümüzdeki 30 günün vakitlerini veritabanından çekip alarmları planlıyoruz
        for (dayOffset in 0..30) {
            val targetDate = today.plusDays(dayOffset.toLong())
            val dateStr = targetDate.toString() // YYYY-MM-DD
            val prayerTimes = prayerTimeRepository.getPrayerTimesByDate(dateStr) ?: continue

            val prayers = listOf(
                "İmsak" to prayerTimes.fajr,
                "Güneş" to prayerTimes.sunrise,
                "Öğle" to prayerTimes.dhuhr,
                "İkindi" to prayerTimes.asr,
                "Akşam" to prayerTimes.maghrib,
                "Yatsı" to prayerTimes.isha
            )

            prayers.forEachIndexed { index, (name, timeStr) ->
                try {
                    val time = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"))
                    val prayerDateTime = LocalDateTime.of(targetDate, time)

                    // Yalnızca şu andan sonraki vakitler için alarm kur
                    if (prayerDateTime.isAfter(now)) {
                        // Her gün ve her vakit için benzersiz (unique) bir Request Code türetiyoruz
                        val requestCode = generateRequestCode(targetDate, index)
                        alarmScheduler.schedule(
                            prayerName = name,
                            triggerTime = prayerDateTime,
                            requestCode = requestCode
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Tarih ve vakit indeksinden benzersiz tamsayı (requestCode) üretir.
     * Örn: 2026-10-16 tarihi için hash + vakit sırası.
     */
    private fun generateRequestCode(date: LocalDate, prayerIndex: Int): Int {
        val dateValue = date.year * 10000 + date.monthValue * 100 + date.dayOfMonth
        return (dateValue % 100000) * 10 + prayerIndex
    }
}