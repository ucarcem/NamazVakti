package com.example.namazvakti.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.namazvakti.ui.main.components.PrayerCountdownHeader

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToLocation: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                // 10. Gün Bileşeni: Büyük Sayaç Kartı
                PrayerCountdownHeader(
                    city = uiState.city,
                    dateFormatted = uiState.currentDateFormatted,
                    nextPrayerName = uiState.nextPrayerName,
                    remainingTimeFormatted = uiState.remainingTimeFormatted,
                    onLocationClick = onNavigateToLocation
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 11. Günde buraya Vakit Listesi (DailyPrayerList) eklenecek.
            }
        }
    }
}