package com.example.namazvakti.ui.qibla

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun QiblaScreen(
    viewModel: QiblaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val compassBgColor by animateColorAsState(
        targetValue = if (uiState.isFacingQibla) Color(0xFF1E8449) else Color(0xFF2C3E50),
        animationSpec = tween(durationMillis = 300),
        label = "compassBg"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2631))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Üst Bilgi Başlığı
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Kıble Pusulası",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${uiState.cityName} için Kıble Açısı: ${uiState.qiblaBearing.toInt()}°",
                fontSize = 18.sp,
                color = Color.LightGray
            )
        }

        // Pusula Gövdesi
        Box(
            modifier = Modifier
                .size(300.dp)
                .background(compassBgColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // 1. Sabit Hedef Çizgisi (Ekranın Tam Üst Ortası)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .width(4.dp)
                    .height(16.dp)
                    .background(Color.White)
            )

            // 2. Sabit Dış Çember
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.width / 2 - 20.dp.toPx()
                drawCircle(
                    color = Color.White.copy(alpha = 0.2f),
                    radius = radius,
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            // 3. Cihaz Yönüne Göre Dönen Kadran ve Kuzey (N) İşaretçisi
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(-uiState.deviceAzimuth)
            ) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2 - 28.dp.toPx()

                // Kırmızı Kuzey (N) Noktası
                drawCircle(
                    color = Color(0xFFE74C3C),
                    radius = 12.dp.toPx(),
                    center = Offset(center.x, center.y - radius)
                )
            }

            // Kuzey "N" Metin Etiketi
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(-uiState.deviceAzimuth),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "N",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 22.dp)
                )
            }

            // 4. Kıble İbresi (Arama esnasında Kırmızı, Hizalanınca Yeşil)
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(uiState.needleRotation)
            ) {
                val center = Offset(size.width / 2, size.height / 2)

                val path = Path().apply {
                    moveTo(center.x, center.y - 105.dp.toPx())
                    lineTo(center.x - 20.dp.toPx(), center.y + 25.dp.toPx())
                    lineTo(center.x, center.y + 10.dp.toPx())
                    lineTo(center.x + 20.dp.toPx(), center.y + 25.dp.toPx())
                    close()
                }

                drawPath(
                    path = path,
                    color = if (uiState.isFacingQibla) Color(0xFF2ECC71) else Color(0xFFE74C3C)
                )
            }

            // Merkez Daire
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(Color.White, CircleShape)
            )
        }

        // Alt Durum Kartı
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.isFacingQibla) Color(0xFF1E8449) else Color(0xFF2C3E50)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (uiState.isFacingQibla) "Kıble Yönündesiniz!" else "Telefonu Çevirin",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (uiState.isFacingQibla)
                        "Hizalalandınız, namazınızı bu yöne doğru kılabilirsiniz."
                    else
                        "Kırmızı oku ekranın üstündeki beyaz çizgiye getirene kadar dönün.",
                    fontSize = 14.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}