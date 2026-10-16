package com.example.namazvakti.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.namazvakti.ui.location.LocationSelectionScreen
import com.example.namazvakti.ui.main.MainScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Şehir Seçim Ekranı
        composable(route = Screen.LocationSelection.route) {
            LocationSelectionScreen(
                onLocationSelected = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.LocationSelection.route) { inclusive = true }
                    }
                }
            )
        }

        // Ana Ekran (MainScreen Entegre Edildi)
        composable(route = Screen.Main.route) {
            MainScreen(
                onNavigateToLocation = {
                    navController.navigate(Screen.LocationSelection.route)
                }
            )
        }
    }
}