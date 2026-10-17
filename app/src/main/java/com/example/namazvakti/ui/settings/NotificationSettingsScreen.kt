package com.example.namazvakti.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.namazvakti.ui.common.NotificationPermissionHandler

private data class PrayerAlarmItem(
    val key: String,
    val label: String,
    val isEnabled: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val prayerList = listOf(
        PrayerAlarmItem("fajr", "İmsak", uiState.fajrAlarmEnabled),
        PrayerAlarmItem("sun", "Güneş", uiState.sunAlarmEnabled),
        PrayerAlarmItem("dhuhr", "Öğle", uiState.dhuhrAlarmEnabled),
        PrayerAlarmItem("asr", "İkindi", uiState.asrAlarmEnabled),
        PrayerAlarmItem("maghrib", "Akşam", uiState.maghribAlarmEnabled),
        PrayerAlarmItem("isha", "Yatsı", uiState.ishaAlarmEnabled)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Uyarı Seçimi", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            NotificationPermissionHandler()

            Text(
                text = "Ezan ve bildirim uyarısı almak istediğiniz vakitleri seçin:",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            prayerList.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.label,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Switch(
                            checked = item.isEnabled,
                            onCheckedChange = { checked ->
                                viewModel.togglePrayerAlarm(item.key, checked)
                            }
                        )
                    }
                }
            }
        }
    }
}