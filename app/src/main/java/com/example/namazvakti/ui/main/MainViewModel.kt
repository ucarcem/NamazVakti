package com.example.namazvakti.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.data.repository.PrayerTimeRepository
import com.example.namazvakti.domain.usecase.SchedulePrayerAlarmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prayerTimeRepository: PrayerTimeRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val schedulePrayerAlarmsUseCase: SchedulePrayerAlarmsUseCase // YENİ INJECT
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeUserSettingsAndPrayerTimes()
        startCountdownTicker()
    }

    private fun observeUserSettingsAndPrayerTimes() {
        viewModelScope.launch {
            userPreferencesRepository.userSettingsFlow.collectLatest { settings ->
                _uiState.update { it.copy(city = settings.selectedCity) }
                loadTodayPrayerTimes()
            }
        }
    }

    fun loadTodayPrayerTimes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val todayDate = LocalDate.now().toString()
            val formattedDate = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd MMMM yyyy, EEEE", Locale("tr"))
            )

            try {
                val prayerTimes = prayerTimeRepository.getPrayerTimesByDate(todayDate)

                if (prayerTimes != null) {
                    _uiState.update {
                        it.copy(
                            todayPrayerTimes = prayerTimes,
                            currentDateFormatted = formattedDate,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    calculateNextPrayerAndRemainingTime()

                    // Vakitler yüklendiğinde gelecek tüm alarmları kuruyoruz
                    schedulePrayerAlarmsUseCase()
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Bugüne ait namaz vakitleri bulunamadı. Lütfen internet bağlantınızı kontrol edip tekrar deneyin."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Veriler yüklenirken bir hata oluştu: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    private fun startCountdownTicker() {
        viewModelScope.launch {
            while (true) {
                calculateNextPrayerAndRemainingTime()
                delay(1000)
            }
        }
    }

    private fun calculateNextPrayerAndRemainingTime() {
        val prayerTimes = _uiState.value.todayPrayerTimes ?: return
        val now = LocalDateTime.now()

        val list = listOf(
            "İmsak" to prayerTimes.fajr,
            "Güneş" to prayerTimes.sunrise,
            "Öğle" to prayerTimes.dhuhr,
            "İkindi" to prayerTimes.asr,
            "Akşam" to prayerTimes.maghrib,
            "Yatsı" to prayerTimes.isha
        )

        var foundNext = false
        for ((name, timeStr) in list) {
            try {
                val time = LocalTime.parse(timeStr)
                val prayerDateTime = LocalDateTime.of(LocalDate.now(), time)

                if (prayerDateTime.isAfter(now)) {
                    val duration = Duration.between(now, prayerDateTime)
                    val formatted = formatDuration(duration)

                    _uiState.update {
                        it.copy(
                            nextPrayerName = name,
                            remainingTimeFormatted = formatted
                        )
                    }
                    foundNext = true
                    break
                }
            } catch (e: Exception) {
                continue
            }
        }

        if (!foundNext) {
            try {
                val tomorrowFajr = LocalDateTime.of(
                    LocalDate.now().plusDays(1),
                    LocalTime.parse(prayerTimes.fajr)
                )
                val duration = Duration.between(now, tomorrowFajr)
                _uiState.update {
                    it.copy(
                        nextPrayerName = "İmsak (Yarın)",
                        remainingTimeFormatted = formatDuration(duration)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        nextPrayerName = "İmsak",
                        remainingTimeFormatted = "00:00:00"
                    )
                }
            }
        }
    }

    private fun formatDuration(duration: Duration): String {
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }
}