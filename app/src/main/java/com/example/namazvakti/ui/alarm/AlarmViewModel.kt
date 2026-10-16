package com.example.namazvakti.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.data.repository.PrayerTimeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class AlarmUiState(
    val cityName: String = "Mersin",
    val prayerName: String = "Namaz",
    val prayerTime: String = "--:--",
    val nextPrayerTime: String = "--:--"
)

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    fun initAlarmData(
        intentCity: String?,
        intentPrayerName: String?,
        intentPrayerTime: String?,
        intentNextPrayerTime: String?
    ) {
        viewModelScope.launch {
            val settings = userPreferencesRepository.userSettingsFlow.first()
            val city = if (!intentCity.isNullOrBlank()) intentCity else settings.selectedCity.ifBlank { "Mersin" }

            // Intent verileri sağlamsa doğrudan kullan
            if (!intentPrayerName.isNullOrBlank() && intentPrayerName != "Namaz" &&
                !intentPrayerTime.isNullOrBlank() && intentPrayerTime != "--:--" &&
                !intentNextPrayerTime.isNullOrBlank() && intentNextPrayerTime != "--:--"
            ) {
                _uiState.update {
                    it.copy(
                        cityName = city,
                        prayerName = intentPrayerName,
                        prayerTime = intentPrayerTime,
                        nextPrayerTime = intentNextPrayerTime
                    )
                }
            } else {
                // Uygulama kapalıyken Intent verileri silindiyse DB'den çek (Fallback)
                val todayDate = LocalDate.now().toString()
                val prayerTimes = prayerTimeRepository.getPrayerTimesByDate(todayDate)

                if (prayerTimes != null) {
                    val currentDetails = getCurrentAndNextPrayerDetails(
                        fajr = prayerTimes.fajr,
                        sun = prayerTimes.sunrise,
                        dhuhr = prayerTimes.dhuhr,
                        asr = prayerTimes.asr,
                        maghrib = prayerTimes.maghrib,
                        isha = prayerTimes.isha
                    )
                    _uiState.update {
                        it.copy(
                            cityName = city,
                            prayerName = currentDetails.prayerName,
                            prayerTime = currentDetails.prayerTime,
                            nextPrayerTime = currentDetails.nextPrayerTime
                        )
                    }
                } else {
                    _uiState.update { it.copy(cityName = city) }
                }
            }
        }
    }

    private data class PrayerDetails(
        val prayerName: String,
        val prayerTime: String,
        val nextPrayerTime: String
    )

    private fun getCurrentAndNextPrayerDetails(
        fajr: String,
        sun: String,
        dhuhr: String,
        asr: String,
        maghrib: String,
        isha: String
    ): PrayerDetails {
        val now = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val prayerList = listOf(
            Triple("İmsak", fajr, sun),
            Triple("Güneş", sun, dhuhr),
            Triple("Öğle", dhuhr, asr),
            Triple("İkindi", asr, maghrib),
            Triple("Akşam", maghrib, isha),
            Triple("Yatsı", isha, fajr)
        )

        for (item in prayerList) {
            val time = try { LocalTime.parse(item.second, formatter) } catch (e: Exception) { LocalTime.MIDNIGHT }
            if (now.isBefore(time)) {
                val prevIndex = (prayerList.indexOf(item) - 1 + prayerList.size) % prayerList.size
                val prev = prayerList[prevIndex]
                return PrayerDetails(prev.first, prev.second, prev.third)
            }
        }
        return PrayerDetails("Yatsı", isha, fajr)
    }
}