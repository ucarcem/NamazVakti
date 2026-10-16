package com.example.namazvakti.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.namazvakti.core.widget.PrayerWidgetUpdater
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.data.repository.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val prayerTimeRepository: PrayerTimeRepository,
    private val prayerWidgetUpdater: PrayerWidgetUpdater // YENİ INJECT
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    fun selectCity(city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // 1. Şehir tercihini DataStore'a kaydet
                userPreferencesRepository.updateSelectedCity(city)

                // 2. Yeni şehrin vakitlerini API'den çek ve Room DB'ye yaz
                prayerTimeRepository.fetchAndSavePrayerTimesForMonth(city)

                // 3. Ana ekrandaki tüm widget'ları anında senkronize et
                prayerWidgetUpdater.updateAllWidgets()

                _uiState.update { it.copy(isLoading = false, isLocationSaved = true) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Şehir vakitleri indirilemedi: ${e.localizedMessage}"
                    )
                }
            }
        }
    }
}