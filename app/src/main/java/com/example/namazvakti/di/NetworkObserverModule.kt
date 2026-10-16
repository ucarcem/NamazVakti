package com.example.namazvakti.di

import com.example.namazvakti.core.network.NetworkObserver
import com.example.namazvakti.core.network.NetworkObserverImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkObserverModule {

    @Binds
    @Singleton
    abstract fun bindNetworkObserver(
        networkObserverImpl: NetworkObserverImpl
    ): NetworkObserver
}