package com.example.namazvakti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.namazvakti.core.theme.FontScale
import com.example.namazvakti.core.theme.NamazVaktiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Test amacıyla LARGE (Büyük) font ölçeği ile başlatıyoruz
            NamazVaktiTheme(fontScale = FontScale.LARGE) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Namaz Vakti",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Text(
                            text = "Öğle: 13:02",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Yaşlı bireyler için büyük ve yüksek kontrastlı görünüm örneği.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}