package com.example.namazvakti.ui.settings

import com.example.namazvakti.core.theme.FontScale

data class SettingsUiState(
    val fontScale: FontScale = FontScale.NORMAL,
    val selectedCity: String = ""
)