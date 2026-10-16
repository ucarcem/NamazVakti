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
                val coords = QiblaCalculator.cityCoordinates[selectedCityName] ?: Pair(36.8121, 34.6415)
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

                // İbrenin dönmesi gereken göreceli açı (Kıble Açısı - Cihaz Açısı)
                val needleRotation = (qiblaBearing - deviceAzimuth + 360) % 360

                // Cihaz Kıble yönüne (±3 derece toleransla) hizalandı mı?
                val isFacingQibla = needleRotation in 357.0f..360.0f || needleRotation in 0.0f..3.0f

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