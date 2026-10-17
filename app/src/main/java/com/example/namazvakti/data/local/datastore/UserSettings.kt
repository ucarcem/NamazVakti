package com.example.namazvakti.data.local.datastore

import androidx.annotation.Keep
import com.example.namazvakti.core.theme.FontScale

@Keep
data class UserSettings(
    val selectedCity: String = "",
    val fontScale: FontScale = FontScale.MEDIUM,
    val fajrAlarmEnabled: Boolean = true,
    val sunAlarmEnabled: Boolean = true,
    val dhuhrAlarmEnabled: Boolean = true,
    val asrAlarmEnabled: Boolean = true,
    val maghribAlarmEnabled: Boolean = true,
    val ishaAlarmEnabled: Boolean = true
)