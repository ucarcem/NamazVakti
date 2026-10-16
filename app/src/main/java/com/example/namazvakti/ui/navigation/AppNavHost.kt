package com.example.namazvakti.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.namazvakti.ui.location.LocationSelectionScreen
import com.example.namazvakti.ui.main.MainScreen
import com.example.namazvakti.ui.qibla.QiblaScreen
import com.example.namazvakti.ui.settings.SettingsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Qibla.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Location.route) {
                LocationSelectionScreen(
                    onLocationSelected = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Location.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                MainScreen(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onNavigateToLocation = {
                        navController.navigate(Screen.Location.route)
                    }
                )
            }

            composable(Screen.Qibla.route) {
                QiblaScreen()
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}