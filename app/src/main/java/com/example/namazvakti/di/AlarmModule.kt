package com.example.namazvakti.di

import com.example.namazvakti.core.alarm.AlarmScheduler
import com.example.namazvakti.core.alarm.PrayerAlarmScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlarmModule {

    @Binds
    @Singleton
    abstract fun bindAlarmScheduler(
        prayerAlarmScheduler: PrayerAlarmScheduler
    ): AlarmScheduler
}