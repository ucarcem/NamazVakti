package com.example.namazvakti.data.mapper

import com.example.namazvakti.data.remote.dto.DateDto
import com.example.namazvakti.data.remote.dto.DailyPrayerDataDto
import com.example.namazvakti.data.remote.dto.GregorianDateDto
import com.example.namazvakti.data.remote.dto.TimingsDto
import org.junit.Assert.assertEquals
import org.junit.Test

class PrayerTimeMapperTest {

    @Test
    fun `toEntity should convert date format from DD-MM-YYYY to YYYY-MM-DD and clean time zone strings`() {
        // GİRDİ (Given): API'den gelen ham DTO verisi
        val dto = DailyPrayerDataDto(
            timings = TimingsDto(
                fajr = "05:12 (+03)",
                sunrise = "06:38 (TRT)",
                dhuhr = "12:54",
                asr = "16:02",
                maghrib = "19:10",
                isha = "20:30"
            ),
            date = DateDto(
                gregorian = GregorianDateDto(date = "15-10-2026")
            )
        )

        // İŞLEM (When): Mapper fonksiyonunun çalıştırılması
        val entity = dto.toEntity()

        // ÇIKTI KONTROLÜ (Then): Beklenen dönüşümlerin doğrulanması
        assertEquals("2026-10-15", entity.date) // Tarih YYYY-MM-DD formatına girmeli
        assertEquals("05:12", entity.fajr)      // Parantez içindeki saat dilimi temizlenmeli
        assertEquals("06:38", entity.sunrise)   // Parantez içindeki saat dilimi temizlenmeli
        assertEquals("12:54", entity.dhuhr)
        assertEquals("16:02", entity.asr)
        assertEquals("19:10", entity.maghrib)
        assertEquals("20:30", entity.isha)
    }
}