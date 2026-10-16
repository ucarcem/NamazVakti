package com.example.namazvakti.di

import com.example.namazvakti.data.local.dao.PrayerTimeDao
import com.example.namazvakti.data.remote.PrayerApi
import com.example.namazvakti.data.repository.PrayerTimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePrayerTimeRepository(
        prayerTimeDao: PrayerTimeDao,
        prayerApi: PrayerApi
    ): PrayerTimeRepository {
        return PrayerTimeRepository(prayerTimeDao, prayerApi)
    }
}