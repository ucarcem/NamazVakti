package com.example.namazvakti.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.namazvakti.core.theme.FontScale
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.domain.usecase.SchedulePrayerAlarmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val schedulePrayerAlarmsUseCase: SchedulePrayerAlarmsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        userPreferencesRepository.userSettingsFlow
            .onEach { settings ->
                _uiState.update {
                    it.copy(
                        fontScale = settings.fontScale,
                        fajrAlarmEnabled = settings.fajrAlarmEnabled,
                        sunAlarmEnabled = settings.sunAlarmEnabled,
                        dhuhrAlarmEnabled = settings.dhuhrAlarmEnabled,
                        asrAlarmEnabled = settings.asrAlarmEnabled,
                        maghribAlarmEnabled = settings.maghribAlarmEnabled,
                        ishaAlarmEnabled = settings.ishaAlarmEnabled
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateFontScale(scale: FontScale) {
        viewModelScope.launch {
            userPreferencesRepository.updateFontScale(scale)
        }
    }

    fun togglePrayerAlarm(prayerKey: String, isEnabled: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updatePrayerAlarmState(prayerKey, isEnabled)
            schedulePrayerAlarmsUseCase()
        }
    }
}