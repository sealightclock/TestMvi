package com.example.jonathan.testmvi.features.users.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.jonathan.testmvi.features.users.data.dto.UserDto
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Handles low-level operations with Preferences DataStore.
 */
class UsersDataStoreApi(context: Context) {

    private val Context.dataStore by preferencesDataStore(name = "users_pref")

    private val dataStore = context.dataStore
    private val usersKey = stringPreferencesKey("users_json")

    suspend fun saveUsers(userList: List<UserDto>) {
        val json = Json.Default.encodeToString(userList)
        dataStore.edit { prefs ->
            prefs[usersKey] = json
        }
    }

    suspend fun loadUsers(): List<UserDto> {
        val prefs = dataStore.data.first()
        val json = prefs[usersKey] ?: return emptyList()
        return runCatching {
            Json.Default.decodeFromString<List<UserDto>>(json)
        }.getOrDefault(emptyList())
    }
}