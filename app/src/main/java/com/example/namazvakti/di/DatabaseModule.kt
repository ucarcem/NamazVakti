package com.example.namazvakti.di

import android.content.Context
import androidx.room.Room
import com.example.namazvakti.data.local.dao.PrayerTimeDao
import com.example.namazvakti.data.local.database.PrayerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePrayerDatabase(
        @ApplicationContext context: Context
    ): PrayerDatabase {
        return Room.databaseBuilder(
            context,
            PrayerDatabase::class.java,
            "prayer_database.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerTimeDao(database: PrayerDatabase): PrayerTimeDao {
        return database.prayerTimeDao()
    }
}