package com.example.namazvakti.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "prayer_times")
data class PrayerTimeEntity(
    @PrimaryKey
    val date: String, // YYYY-MM-DD formatında tarih (Örn: "2026-10-15")
    val fajr: String,    // İmsak
    val sunrise: String, // Güneş
    val dhuhr: String,   // Öğle
    val asr: String,     // İkindi
    val maghrib: String, // Akşam
    val isha: String     // Yatsı
)