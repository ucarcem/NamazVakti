package com.example.namazvakti.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.namazvakti.ui.common.NotificationPermissionHandler
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

            // Android 13+ Bildirim İzni Uyarı Kartı
            NotificationPermissionHandler()

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.errorMessage != null -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Veri Bulunamadı",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = uiState.errorMessage ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { viewModel.loadTodayPrayerTimes() },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Yeniden Dene",
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }

                else -> {
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
}