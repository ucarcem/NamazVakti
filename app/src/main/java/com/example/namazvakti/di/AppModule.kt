package com.example.namazvakti.di

import android.content.Context
import androidx.room.Room
import com.example.namazvakti.data.local.NamazVaktiDatabase
import com.example.namazvakti.data.local.dao.PrayerTimeDao
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNamazVaktiDatabase(
        @ApplicationContext context: Context
    ): NamazVaktiDatabase {
        return Room.databaseBuilder(
            context,
            NamazVaktiDatabase::class.java,
            "namaz_vakti_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerTimeDao(db: NamazVaktiDatabase): PrayerTimeDao {
        return db.prayerTimeDao
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        @ApplicationContext context: Context
    ): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}