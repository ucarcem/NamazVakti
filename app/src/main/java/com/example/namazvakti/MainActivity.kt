package com.example.namazvakti

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.example.namazvakti.data.local.datastore.UserPreferencesRepository
import com.example.namazvakti.core.theme.NamazVaktiTheme
import com.example.namazvakti.ui.navigation.NavGraph
import com.example.namazvakti.ui.navigation.Screen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userSettings by userPreferencesRepository.userSettingsFlow
                .collectAsStateWithLifecycle(initialValue = null)

            userSettings?.let { settings ->
                NamazVaktiTheme(fontScale = settings.fontScale) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val navController = rememberNavController()
                        val startDestination = if (settings.selectedCity.isNotBlank()) {
                            Screen.Main.route
                        } else {
                            Screen.LocationSelection.route
                        }

                        NavGraph(
                            navController = navController,
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}