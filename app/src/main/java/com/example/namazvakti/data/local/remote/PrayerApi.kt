package com.example.namazvakti.data.remote

import com.example.namazvakti.data.remote.dto.PrayerTimesResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerApi {

    @GET("v1/calendarByCity/{year}/{month}")
    suspend fun getMonthlyPrayerTimes(
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Query("city") city: String,
        @Query("country") country: String = "Turkey",
        @Query("method") method: Int = 13 // 13 = Diyanet İşleri Başkanlığı
    ): PrayerTimesResponseDto

    companion object {
        const val BASE_URL = "https://api.aladhan.com/"
    }
}