package com.example.namazvakti.ui.alarm

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import com.example.namazvakti.R
import com.example.namazvakti.core.theme.NamazVaktiTheme
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class AlarmActivity : ComponentActivity() {

    companion object {
        private var mediaPlayer: MediaPlayer? = null

        fun startAlarmSound(context: Context) {
            if (mediaPlayer == null) {
                try {
                    // res/raw/ezan.mp3 varsa onu çalar, yoksa varsayılan alarm sesini çalar
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
                    mediaPlayer?.isLooping = false
                    mediaPlayer?.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun stopAlarmSound() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cihaz kilitliyse ekranı yak ve kilit üstünde göster
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

        val cityName = intent.getStringExtra("CITY_NAME") ?: "Mersin"
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Vakit"
        val prayerTime = intent.getStringExtra("PRAYER_TIME") ?: "--:--"
        val nextPrayerTime = intent.getStringExtra("NEXT_PRAYER_TIME") ?: "--:--"

        startAlarmSound(this)

        setContent {
            NamazVaktiTheme {
                AlarmScreenContent(
                    cityName = cityName,
                    prayerName = prayerName,
                    prayerTime = prayerTime,
                    nextPrayerTime = nextPrayerTime,
                    onCloseClick = {
                        // Ekran kapanır ama Ezan sesi stopAlarmSound() çağrılmadığı için çalmaya devam eder
                        finish()
                    },
                    onMuteClick = {
                        stopAlarmSound()
                    }
                )
            }
        }
    }

    // Fiziksel Ses Kısma Tuşuna basıldığında sesi sustur
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
    var remainingTimeText by remember { mutableStateOf("--:--:--") }

    // Kalan süreyi saniye saniye hesaplayan Sayaç
    LaunchedEffect(Unit) {
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

            // Şehir ve Vakit İsmi
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = cityName,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "$prayerName Ezanı Okunuyor",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2ECC71),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Vakit Saati: $prayerTime",
                    fontSize = 22.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Kalan Süre Kartı
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Namazı Kılmak İçin Kalan Süre",
                        fontSize = 18.sp,
                        color = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = remainingTimeText,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Butonlar
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Sesi Sustur Butonu
                OutlinedButton(
                    onClick = onMuteClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Sesi Sustur", fontSize = 18.sp, color = Color.White)
                }

                // Ekranı Kapat Butonu (Ezan çalmaya devam eder)
                Button(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E8449))
                ) {
                    Text("Ekranı Kapat", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}