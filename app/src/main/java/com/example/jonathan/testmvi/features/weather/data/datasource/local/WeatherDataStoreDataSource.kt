package com.example.jonathan.testmvi.features.weather.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "weather_prefs")

object WeatherDataStoreDataSource {
    private val KEY_LAST_LOCATION = stringPreferencesKey("weather_last_location")

    fun lastLocationFlow(context: Context): Flow<String?> {
        return context.dataStore.data.map { prefs ->
            prefs[KEY_LAST_LOCATION]
        }
    }

    suspend fun saveLastLocation(context: Context, location: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_LOCATION] = location
        }
    }
}
