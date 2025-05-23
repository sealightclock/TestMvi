package com.example.jonathan.testmvi.shared.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the name of the preferences DataStore
private const val PERMISSION_PREFS_NAME = "permission_prefs"

val Context.permissionDataStore by preferencesDataStore(name = PERMISSION_PREFS_NAME)

object PermissionKeys {
    val FineLocationRequested = booleanPreferencesKey("fine_location_requested")
    val BackgroundLocationRequested = booleanPreferencesKey("background_location_requested")
}

object PermissionPreferences {
    fun hasRequestedFineLocation(context: Context): Flow<Boolean> =
        context.permissionDataStore.data.map { prefs ->
            prefs[PermissionKeys.FineLocationRequested] ?: false
        }

    fun hasRequestedBackgroundLocation(context: Context): Flow<Boolean> =
        context.permissionDataStore.data.map { prefs ->
            prefs[PermissionKeys.BackgroundLocationRequested] ?: false
        }

    suspend fun markFineLocationRequested(context: Context) {
        context.permissionDataStore.edit { prefs ->
            prefs[PermissionKeys.FineLocationRequested] = true
        }
    }

    suspend fun markBackgroundLocationRequested(context: Context) {
        context.permissionDataStore.edit { prefs ->
            prefs[PermissionKeys.BackgroundLocationRequested] = true
        }
    }
}
