package com.example.namazvakti.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.namazvakti.data.local.entity.PrayerTimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerTimeDao {

    // Seçilen tarihe ait ezan vakitlerini getirir
    @Query("SELECT * FROM prayer_times WHERE date = :date LIMIT 1")
    suspend fun getPrayerTimesByDate(date: String): PrayerTimeEntity?

    // Tüm ayın vakitlerini canlı akış (Flow) olarak dinler
    @Query("SELECT * FROM prayer_times ORDER BY date ASC")
    fun getAllPrayerTimes(): Flow<List<PrayerTimeEntity>>

    // API'den gelen aylık vakitleri toplu şekilde veritabanına yazar (çakışma varsa günceller)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimes(prayerTimes: List<PrayerTimeEntity>)

    // Veritabanını temizler (Şehir değişikliği durumunda kullanılır)
    @Query("DELETE FROM prayer_times")
    suspend fun clearAllPrayerTimes()
}