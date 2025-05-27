package com.example.jonathan.testmvi.shared.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.screenDataStore by preferencesDataStore(name = "screen_prefs")

object ScreenDataStore {
    private val LAST_SCREEN_KEY = stringPreferencesKey("last_screen")

    fun lastScreenFlow(context: Context): Flow<String?> {
        return context.screenDataStore.data.map { preferences ->
            preferences[LAST_SCREEN_KEY]
        }
    }

    suspend fun saveLastScreen(context: Context, route: String) {
        context.screenDataStore.edit { preferences ->
            preferences[LAST_SCREEN_KEY] = route
        }
    }
}
