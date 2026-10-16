package com.example.namazvakti.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.namazvakti.core.theme.FontScale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val SELECTED_CITY = stringPreferencesKey("selected_city")
        val FONT_SCALE = stringPreferencesKey("font_scale")
    }

    val userSettingsFlow: Flow<UserSettings> = dataStore.data.map { preferences ->
        val city = preferences[PreferencesKeys.SELECTED_CITY] ?: ""
        val fontScaleName = preferences[PreferencesKeys.FONT_SCALE] ?: FontScale.NORMAL.name
        val fontScale = try {
            FontScale.valueOf(fontScaleName)
        } catch (e: Exception) {
            FontScale.NORMAL
        }
        UserSettings(selectedCity = city, fontScale = fontScale)
    }

    suspend fun updateSelectedCity(city: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CITY] = city
        }
    }

    suspend fun updateFontScale(fontScale: FontScale) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SCALE] = fontScale.name
        }
    }
}