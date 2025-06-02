package com.example.jonathan.testmvi.features.weather.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Provides access to weather-related preferences using DataStore.
 */
class WeatherDataStoreDataSource(context: Context) {
    private val appContext = context.applicationContext
    private val key = stringPreferencesKey("weather_last_location")

    fun lastLocationFlow(): Flow<String?> {
        return appContext.weatherDataStore.data.map { prefs ->
            prefs[key]
        }
    }

    suspend fun saveLastLocation(location: String) {
        appContext.weatherDataStore.edit { prefs ->
            prefs[key] = location
        }
    }
}
