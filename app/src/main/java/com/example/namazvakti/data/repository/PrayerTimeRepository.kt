package com.example.namazvakti.data.repository

import com.example.namazvakti.data.local.dao.PrayerTimeDao
import com.example.namazvakti.data.local.entity.PrayerTimeEntity
import com.example.namazvakti.data.remote.PrayerApi
import com.example.namazvakti.data.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrayerTimeRepository @Inject constructor(
    private val prayerApi: PrayerApi,
    private val prayerTimeDao: PrayerTimeDao
) {

    suspend fun getPrayerTimesByDate(date: String): PrayerTimeEntity? {
        return withContext(Dispatchers.IO) {
            prayerTimeDao.getPrayerTimesByDate(date)
        }
    }

    suspend fun fetchAndSavePrayerTimesForMonth(city: String) {
        withContext(Dispatchers.IO) {
            try {
                val now = LocalDate.now()
                val response = prayerApi.getMonthlyPrayerTimes(
                    year = now.year,
                    month = now.monthValue,
                    city = city,
                    country = "Turkey",
                    method = 13
                )

                if (response.code == 200 && response.data.isNotEmpty()) {
                    val entities = response.data.map { it.toEntity() }
                    prayerTimeDao.insertPrayerTimes(entities)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}