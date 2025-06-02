package com.example.jonathan.testmvi.features.weather.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "weather_prefs")

/**
 * Handles low-level DataStore access for weather data.
 */
class WeatherDataStoreApi(context: Context) {

    private val dataStore = context.dataStore
    private val lastLocationKey = stringPreferencesKey("weather_last_location")

    fun lastLocationFlow() = dataStore.data.map { prefs ->
        prefs[lastLocationKey]
    }

    suspend fun saveLastLocation(location: String) {
        dataStore.edit { prefs ->
            prefs[lastLocationKey] = location
        }
    }

    suspend fun loadLastLocation(): String? {
        return dataStore.data.first()[lastLocationKey]
    }
}
