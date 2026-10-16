package com.example.namazvakti.ui.main

import com.example.namazvakti.data.local.entity.PrayerTimeEntity

data class MainUiState(
    val city: String = "",
    val todayPrayerTimes: PrayerTimeEntity? = null,
    val nextPrayerName: String = "",
    val remainingTimeFormatted: String = "00:00:00",
    val currentDateFormatted: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isOnline: Boolean = true
)