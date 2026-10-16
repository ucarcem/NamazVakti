package com.example.namazvakti.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.namazvakti.ui.location.LocationSelectionScreen

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

        // Ana Ekran (10. Günde kodlanacak geçici görünüm)
        composable(route = Screen.Main.route) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ana Ekran (10. Günde Tasarlanacak)",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}