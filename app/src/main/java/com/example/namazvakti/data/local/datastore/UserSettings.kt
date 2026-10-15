package com.example.namazvakti.data.local.datastore

import com.example.namazvakti.core.theme.FontScale

data class UserSettings(
    val selectedCity: String = "İstanbul",
    val selectedDistrict: String = "",
    val fontScale: FontScale = FontScale.NORMAL
)