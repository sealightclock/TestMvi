package com.example.jonathan.testmvi.features.weather.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * Provides a singleton Preferences DataStore instance for the weather feature.
 */
val Context.weatherDataStore by preferencesDataStore(name = "weather_prefs")
