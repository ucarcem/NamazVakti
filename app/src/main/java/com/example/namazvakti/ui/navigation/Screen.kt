package com.example.namazvakti.ui.navigation

sealed class Screen(val route: String) {
    object LocationSelection : Screen("location_selection_screen")
    object Main : Screen("main_screen")
}