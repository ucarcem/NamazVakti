package com.example.namazvakti.ui.settings

import com.example.namazvakti.core.theme.FontScale

data class SettingsUiState(
    val fontScale: FontScale = FontScale.MEDIUM,
    val fajrAlarmEnabled: Boolean = true,
    val sunAlarmEnabled: Boolean = true,
    val dhuhrAlarmEnabled: Boolean = true,
    val asrAlarmEnabled: Boolean = true,
    val maghribAlarmEnabled: Boolean = true,
    val ishaAlarmEnabled: Boolean = true
)