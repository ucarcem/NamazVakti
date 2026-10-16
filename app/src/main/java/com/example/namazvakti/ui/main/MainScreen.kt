package com.example.namazvakti.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.namazvakti.ui.main.components.DailyPrayerList
import com.example.namazvakti.ui.main.components.PrayerCountdownHeader

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToLocation: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // Sayaç Kartı (Header)
                PrayerCountdownHeader(
                    city = uiState.city,
                    dateFormatted = uiState.currentDateFormatted,
                    nextPrayerName = uiState.nextPrayerName,
                    remainingTimeFormatted = uiState.remainingTimeFormatted,
                    onLocationClick = onNavigateToLocation,
                    onSettingsClick = onNavigateToSettings
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 6 Vakit Listesi
                uiState.todayPrayerTimes?.let { prayerTimes ->
                    DailyPrayerList(
                        prayerTimes = prayerTimes,
                        nextPrayerName = uiState.nextPrayerName
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}