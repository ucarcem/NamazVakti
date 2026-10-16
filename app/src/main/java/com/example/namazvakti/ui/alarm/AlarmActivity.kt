package com.example.namazvakti.ui.alarm

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.namazvakti.core.theme.NamazVaktiTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AlarmActivity : ComponentActivity() {

    private val viewModel: AlarmViewModel by viewModels()

    companion object {
        private var mediaPlayer: MediaPlayer? = null

        fun startAlarmSound(context: Context) {
            if (mediaPlayer == null) {
                try {
                    val rawId = context.resources.getIdentifier("ezan", "raw", context.packageName)
                    mediaPlayer = if (rawId != 0) {
                        MediaPlayer.create(context, rawId)
                    } else {
                        val alarmUri = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM)
                        MediaPlayer().apply {
                            setDataSource(context, alarmUri)
                            setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_ALARM)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                    .build()
                            )
                            prepare()
                        }
                    }

                    // En fazla 10 defa çalacak sayaç mantığı
                    var playCount = 0
                    mediaPlayer?.setOnCompletionListener { mp ->
                        playCount++
                        if (playCount < 10) {
                            mp.start()
                        } else {
                            stopAlarmSound()
                        }
                    }

                    mediaPlayer?.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun stopAlarmSound() {
            try {
                mediaPlayer?.stop()
                mediaPlayer?.release()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                mediaPlayer = null
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            @Suppress("DEPRECATION")
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        val intentCity = intent.getStringExtra("CITY_NAME")
        val intentPrayerName = intent.getStringExtra("PRAYER_NAME")
        val intentPrayerTime = intent.getStringExtra("PRAYER_TIME")
        val intentNextPrayerTime = intent.getStringExtra("NEXT_PRAYER_TIME")

        viewModel.initAlarmData(intentCity, intentPrayerName, intentPrayerTime, intentNextPrayerTime)

        startAlarmSound(this)

        setContent {
            NamazVaktiTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AlarmScreenContent(
                    cityName = uiState.cityName,
                    prayerName = uiState.prayerName,
                    prayerTime = uiState.prayerTime,
                    nextPrayerTime = uiState.nextPrayerTime,
                    onCloseClick = { finish() },
                    onMuteClick = { stopAlarmSound() }
                )
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            stopAlarmSound()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

@Composable
fun AlarmScreenContent(
    cityName: String,
    prayerName: String,
    prayerTime: String,
    nextPrayerTime: String,
    onCloseClick: () -> Unit,
    onMuteClick: () -> Unit
) {
    var remainingTimeText by remember(nextPrayerTime) { mutableStateOf("--:--:--") }

    LaunchedEffect(nextPrayerTime) {
        if (nextPrayerTime == "--:--" || nextPrayerTime.isBlank()) return@LaunchedEffect
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        while (true) {
            try {
                val now = LocalTime.now()
                val targetTime = LocalTime.parse(nextPrayerTime, formatter)
                val duration = if (now.isBefore(targetTime)) {
                    Duration.between(now, targetTime)
                } else {
                    Duration.between(now, targetTime).plusDays(1)
                }
                val hours = duration.toHours()
                val minutes = duration.toMinutes() % 60
                val seconds = duration.seconds % 60
                remainingTimeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            } catch (e: Exception) {
                remainingTimeText = "--:--:--"
            }
            delay(1000)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1B2631)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Şehir ve Vakit İsmi (Ortalanmış)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = cityName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$prayerName Ezanı Okunuyor",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2ECC71),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Vakit Saati: $prayerTime",
                    fontSize = 22.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Kalan Süre Kartı (Ortalanmış)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Namazı Kılmak İçin Kalan Süre",
                        fontSize = 18.sp,
                        color = Color.LightGray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = remainingTimeText,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Butonlar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onMuteClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sesi Sustur", fontSize = 18.sp, color = Color.White, textAlign = TextAlign.Center)
                }

                Button(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8449))
                ) {
                    Text("Ekranı Kapat", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                }
            }
        }
    }
}