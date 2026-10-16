package com.example.namazvakti.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String? = null, val icon: ImageVector? = null) {
    object Location : Screen("location_screen")
    object Home : Screen("home_screen", "Ana Ekran", Icons.Default.Home)
    object Qibla : Screen("qibla_screen", "Kıble", Icons.Default.Place)
    object Settings : Screen("settings_screen", "Ayarlar", Icons.Default.Settings)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Qibla,
    Screen.Settings
)