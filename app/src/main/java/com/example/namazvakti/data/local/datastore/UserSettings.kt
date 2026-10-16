package com.example.namazvakti.data.local.datastore

import com.example.namazvakti.core.theme.FontScale

data class UserSettings(
    val selectedCity: String = "Mersin",
    val fontScale: FontScale = FontScale.MEDIUM,
    val fajrAlarmEnabled: Boolean = true,
    val sunAlarmEnabled: Boolean = true,
    val dhuhrAlarmEnabled: Boolean = true,
    val asrAlarmEnabled: Boolean = true,
    val maghribAlarmEnabled: Boolean = true,
    val ishaAlarmEnabled: Boolean = true
)