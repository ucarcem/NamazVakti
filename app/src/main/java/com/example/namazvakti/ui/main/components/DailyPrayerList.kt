package com.example.namazvakti.ui.main.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.namazvakti.data.local.entity.PrayerTimeEntity

@Composable
fun DailyPrayerList(
    prayerTimes: PrayerTimeEntity,
    nextPrayerName: String,
    modifier: Modifier = Modifier
) {
    val prayerList = listOf(
        "İmsak" to prayerTimes.fajr,
        "Güneş" to prayerTimes.sunrise,
        "Öğle" to prayerTimes.dhuhr,
        "İkindi" to prayerTimes.asr,
        "Akşam" to prayerTimes.maghrib,
        "Yatsı" to prayerTimes.isha
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        prayerList.forEach { (name, time) ->
            val isNext = nextPrayerName.contains(name, ignoreCase = true)
            PrayerTimeItem(
                prayerName = name,
                prayerTime = time,
                isNext = isNext
            )
        }
    }
}