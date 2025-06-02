package com.example.jonathan.testmvi.features.users.data.datasource.local

import com.example.jonathan.testmvi.features.users.data.dto.UserDto

/**
 * Handles only data-layer models (DTOs) and delegates persistence to UsersDataStoreApi.
 * Domain-layer entities should not be visible here.
 */
class UsersDataStoreDataSource(private val dataStoreApi: UsersDataStoreApi) {

    suspend fun getUsers(): List<UserDto> {
        return dataStoreApi.loadUsers()
    }

    suspend fun storeUsers(users: List<UserDto>) {
        dataStoreApi.saveUsers(users)
    }
}
