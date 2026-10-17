package com.example.namazvakti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.namazvakti.core.theme.NamazVaktiTheme
import com.example.namazvakti.ui.main.MainViewModel
import com.example.namazvakti.ui.navigation.AppNavHost
import com.example.namazvakti.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            val startDestination = if (uiState.city.isBlank()) {
                Screen.Location.route
            } else {
                Screen.Home.route
            }

            NamazVaktiTheme(fontScale = uiState.fontScale) {
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}