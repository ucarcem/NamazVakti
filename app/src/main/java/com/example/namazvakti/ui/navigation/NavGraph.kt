package com.example.namazvakti.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.namazvakti.ui.location.LocationSelectionScreen
import com.example.namazvakti.ui.main.MainScreen
import com.example.namazvakti.ui.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.LocationSelection.route) {
            LocationSelectionScreen(
                onLocationSelected = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.LocationSelection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = Screen.Main.route) {
            MainScreen(
                onNavigateToLocation = {
                    navController.navigate(Screen.LocationSelection.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        // Ayarlar Ekranı Rotalaması
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}