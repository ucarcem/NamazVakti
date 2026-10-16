package com.example.namazvakti.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.namazvakti.core.theme.FontScale
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.userSettingsFlow.collect { settings ->
                _uiState.update {
                    it.copy(
                        fontScale = settings.fontScale,
                        selectedCity = settings.selectedCity
                    )
                }
            }
        }
    }

    fun updateFontScale(fontScale: FontScale) {
        viewModelScope.launch {
            userPreferencesRepository.updateFontScale(fontScale)
        }
    }
}