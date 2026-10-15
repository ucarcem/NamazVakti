package com.example.namazvakti.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.namazvakti.core.theme.FontScale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val SELECTED_CITY = stringPreferencesKey("selected_city")
        val SELECTED_DISTRICT = stringPreferencesKey("selected_district")
        val FONT_SCALE = stringPreferencesKey("font_scale")
    }

    // Kullanıcı tercihlerini canlı olarak dinleyen Flow akışı
    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        val city = preferences[PreferencesKeys.SELECTED_CITY] ?: "Mersin"
        val district = preferences[PreferencesKeys.SELECTED_DISTRICT] ?: "Toroslar"
        val fontScaleName = preferences[PreferencesKeys.FONT_SCALE] ?: FontScale.NORMAL.name

        val fontScale = try {
            FontScale.valueOf(fontScaleName)
        } catch (e: Exception) {
            FontScale.NORMAL
        }

        UserSettings(
            selectedCity = city,
            selectedDistrict = district,
            fontScale = fontScale
        )
    }

    // Şehir ve İlçe Bilgisini Günceller
    suspend fun updateCityAndDistrict(city: String, district: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CITY] = city
            preferences[PreferencesKeys.SELECTED_DISTRICT] = district
        }
    }

    // Font Ölçeğini Günceller (Small / Normal / Large)
    suspend fun updateFontScale(fontScale: FontScale) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SCALE] = fontScale.name
        }
    }
}