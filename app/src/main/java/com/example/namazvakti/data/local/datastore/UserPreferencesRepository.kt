package com.example.namazvakti.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.namazvakti.core.theme.FontScale
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val SELECTED_CITY = stringPreferencesKey("selected_city")
        val FONT_SCALE = stringPreferencesKey("font_scale")
        val FAJR_ALARM = booleanPreferencesKey("fajr_alarm")
        val SUN_ALARM = booleanPreferencesKey("sun_alarm")
        val DHUHR_ALARM = booleanPreferencesKey("dhuhr_alarm")
        val ASR_ALARM = booleanPreferencesKey("asr_alarm")
        val MAGHRIB_ALARM = booleanPreferencesKey("maghrib_alarm")
        val ISHA_ALARM = booleanPreferencesKey("isha_alarm")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            selectedCity = preferences[PreferencesKeys.SELECTED_CITY] ?: "Mersin",
            fontScale = FontScale.valueOf(
                preferences[PreferencesKeys.FONT_SCALE] ?: FontScale.MEDIUM.name
            ),
            fajrAlarmEnabled = preferences[PreferencesKeys.FAJR_ALARM] ?: true,
            sunAlarmEnabled = preferences[PreferencesKeys.SUN_ALARM] ?: true,
            dhuhrAlarmEnabled = preferences[PreferencesKeys.DHUHR_ALARM] ?: true,
            asrAlarmEnabled = preferences[PreferencesKeys.ASR_ALARM] ?: true,
            maghribAlarmEnabled = preferences[PreferencesKeys.MAGHRIB_ALARM] ?: true,
            ishaAlarmEnabled = preferences[PreferencesKeys.ISHA_ALARM] ?: true
        )
    }

    suspend fun updateSelectedCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_CITY] = city
        }
    }

    suspend fun updateFontScale(fontScale: FontScale) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SCALE] = fontScale.name
        }
    }

    suspend fun updatePrayerAlarmState(prayerKey: String, isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            when (prayerKey) {
                "fajr" -> preferences[PreferencesKeys.FAJR_ALARM] = isEnabled
                "sun" -> preferences[PreferencesKeys.SUN_ALARM] = isEnabled
                "dhuhr" -> preferences[PreferencesKeys.DHUHR_ALARM] = isEnabled
                "asr" -> preferences[PreferencesKeys.ASR_ALARM] = isEnabled
                "maghrib" -> preferences[PreferencesKeys.MAGHRIB_ALARM] = isEnabled
                "isha" -> preferences[PreferencesKeys.ISHA_ALARM] = isEnabled
            }
        }
    }
}