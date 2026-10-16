package com.example.namazvakti.domain.usecase

import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckInitialSetupUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {
    // Kullanıcının kayıtlı bir şehir tercihi olup olmadığını kontrol eder
    operator fun invoke(): Flow<Boolean> {
        return userPreferencesRepository.userSettingsFlow.map { settings ->
            settings.selectedCity.isNotBlank()
        }
    }
}