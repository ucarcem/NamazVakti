package com.example.namazvakti.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.namazvakti.data.local.dao.PrayerTimeDao
import com.example.namazvakti.data.local.entity.PrayerTimeEntity

@Database(
    entities = [PrayerTimeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NamazVaktiDatabase : RoomDatabase() {
    abstract val prayerTimeDao: PrayerTimeDao
}