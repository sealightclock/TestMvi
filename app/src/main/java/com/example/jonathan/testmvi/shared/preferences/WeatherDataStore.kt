package com.example.jonathan.testmvi.shared.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "weather_prefs")
