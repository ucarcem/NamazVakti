package com.example.namazvakti.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun getScaledTypography(fontScale: FontScale): Typography {
    val factor = fontScale.scale

    return Typography(
        // Ana Sayaç ve Büyük Vakit İsimleri
        displayLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = (36 * factor).sp,
            lineHeight = (42 * factor).sp
        ),
        // Kart Başlıkları (Örn: "ÖĞLE NAMAZI")
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold,
            fontSize = (22 * factor).sp,
            lineHeight = (28 * factor).sp
        ),
        // Vakit Saatleri (Örn: "13:02")
        titleMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.SemiBold,
            fontSize = (18 * factor).sp,
            lineHeight = (24 * factor).sp
        ),
        // Genel İçerik Metinleri
        bodyLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = (16 * factor).sp,
            lineHeight = (22 * factor).sp
        ),
        // Açıklama ve Küçük Bilgiler
        bodyMedium = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (14 * factor).sp,
            lineHeight = (20 * factor).sp
        )
    )
}