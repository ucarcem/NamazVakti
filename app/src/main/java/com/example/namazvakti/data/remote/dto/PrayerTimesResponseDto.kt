package com.example.namazvakti.data.remote.dto

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class PrayerTimesResponseDto(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<DailyPrayerDataDto>?
)

@Keep
data class DailyPrayerDataDto(
    @SerializedName("timings") val timings: TimingsDto,
    @SerializedName("date") val date: DateDto
)

@Keep
data class TimingsDto(
    @SerializedName("Fajr") val fajr: String,
    @SerializedName("Sunrise") val sunrise: String,
    @SerializedName("Dhuhr") val dhuhr: String,
    @SerializedName("Asr") val asr: String,
    @SerializedName("Maghrib") val maghrib: String,
    @SerializedName("Isha") val isha: String
)

@Keep
data class DateDto(
    @SerializedName("gregorian") val gregorian: GregorianDateDto
)

@Keep
data class GregorianDateDto(
    @SerializedName("date") val date: String // DD-MM-YYYY formatı
)