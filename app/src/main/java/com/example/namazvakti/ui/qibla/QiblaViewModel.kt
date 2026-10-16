package com.example.namazvakti.ui.qibla

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.namazvakti.core.sensor.CompassSensorManager
import com.example.namazvakti.core.utils.QiblaCalculator
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class QiblaUiState(
    val cityName: String = "Mersin",
    val deviceAzimuth: Float = 0f,
    val qiblaBearing: Float = 0f,
    val needleRotation: Float = 0f,
    val isFacingQibla: Boolean = false,
    val sensorAccuracy: Int = 3
)

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val compassSensorManager: CompassSensorManager,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QiblaUiState())
    val uiState: StateFlow<QiblaUiState> = _uiState.asStateFlow()

    init {
        observeCityAndCalculateQibla()
        observeCompassSensor()
    }

    private fun observeCityAndCalculateQibla() {
        userPreferencesRepository.userSettingsFlow
            .map { it.selectedCity }
            .onEach { city ->
                val selectedCityName = city.ifBlank { "Mersin" }

                // Harf büyüklüğüne duyarsız şehir eşleme (Fallback korumalı)
                val matchedCityKey = QiblaCalculator.cityCoordinates.keys.find {
                    it.equals(selectedCityName.trim(), ignoreCase = true)
                }
                val coords = QiblaCalculator.cityCoordinates[matchedCityKey] ?: Pair(36.8121, 34.6415)
                val qiblaAngle = QiblaCalculator.calculateQiblaBearing(coords.first, coords.second)

                _uiState.update {
                    it.copy(
                        cityName = selectedCityName,
                        qiblaBearing = qiblaAngle
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCompassSensor() {
        compassSensorManager.getCompassFlow()
            .onEach { sensorData ->
                val deviceAzimuth = sensorData.azimuth
                val qiblaBearing = _uiState.value.qiblaBearing

                // İbrenin dönmesi gereken bağıl açı
                val needleRotation = (qiblaBearing - deviceAzimuth + 360) % 360

                // Tolerans ±8 dereceye genişletildi (Kolay yakalama)
                val isFacingQibla = needleRotation <= 8.0f || needleRotation >= 352.0f

                _uiState.update {
                    it.copy(
                        deviceAzimuth = deviceAzimuth,
                        needleRotation = needleRotation,
                        isFacingQibla = isFacingQibla,
                        sensorAccuracy = sensorData.accuracy
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}