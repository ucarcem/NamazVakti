package com.example.namazvakti.data.mapper

import com.example.namazvakti.data.local.entity.PrayerTimeEntity
import com.example.namazvakti.data.remote.dto.DailyPrayerDataDto

fun DailyPrayerDataDto.toEntity(): PrayerTimeEntity {
    // Aladhan API tarihi "15-10-2026" (DD-MM-YYYY) olarak verir.
    // Room DB'de tarih sıralamasının doğru çalışması için "2026-10-15" (YYYY-MM-DD) formatına dönüştürüyoruz.
    val rawDate = date.gregorian.date
    val parts = rawDate.split("-")
    val formattedDate = if (parts.size == 3) {
        "${parts[2]}-${parts[1]}-${parts[0]}"
    } else {
        rawDate
    }

    // Saat dizesinde varsa ekstra ifadeleri temizler (Örn: "05:12 (EEST)" -> "05:12")
    fun cleanTime(time: String): String = time.split(" ")[0]

    return PrayerTimeEntity(
        date = formattedDate,
        fajr = cleanTime(timings.fajr),
        sunrise = cleanTime(timings.sunrise),
        dhuhr = cleanTime(timings.dhuhr),
        asr = cleanTime(timings.asr),
        maghrib = cleanTime(timings.maghrib),
        isha = cleanTime(timings.isha)
    )
}