package com.example.namazvakti.data.repository

import com.example.namazvakti.data.local.dao.PrayerTimeDao
import com.example.namazvakti.data.local.entity.PrayerTimeEntity
import com.example.namazvakti.data.mapper.toEntity
import com.example.namazvakti.data.remote.PrayerApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimeRepository @Inject constructor(
    private val prayerTimeDao: PrayerTimeDao,
    private val prayerApi: PrayerApi
) {

    // Veritabanındaki tüm ezan vakitlerini canlı akış (Flow) olarak sunar
    fun getAllPrayerTimes(): Flow<List<PrayerTimeEntity>> {
        return prayerTimeDao.getAllPrayerTimes()
    }

    // Belirli bir tarihe ait vakti veritabanından getirir
    suspend fun getPrayerTimesByDate(date: String): PrayerTimeEntity? {
        return prayerTimeDao.getPrayerTimesByDate(date)
    }

    // Offline-First Stratejisi: API'den veriyi çeker, Mapper ile dönüştürür ve Room DB'ye yazar
    suspend fun refreshMonthlyPrayerTimes(
        year: Int,
        month: Int,
        city: String
    ): Result<Unit> {
        return try {
            val response = prayerApi.getMonthlyPrayerTimes(
                year = year,
                month = month,
                city = city
            )

            if (response.code == 200 && response.data.isNotEmpty()) {
                val entities = response.data.map { it.toEntity() }
                prayerTimeDao.insertPrayerTimes(entities)
                Result.success(Unit)
            } else {
                Result.failure(Exception("API Yanıtı Başarısız: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Şehir/İlçe değiştiğinde eski veritabanı verilerini temizler
    suspend fun clearLocalData() {
        prayerTimeDao.clearAllPrayerTimes()
    }
}