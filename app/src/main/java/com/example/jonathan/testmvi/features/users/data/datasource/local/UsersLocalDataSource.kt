package com.example.jonathan.testmvi.features.users.data.datasource.local

import com.example.jonathan.testmvi.features.users.data.dto.toDto
import com.example.jonathan.testmvi.features.users.data.dto.toEntity
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Converts between domain and data models and delegates to UsersDataStoreApi.
 */
class UsersLocalDataSource(private val dataStoreApi: UsersDataStoreApi) {

    suspend fun getUsers(): List<UserEntity> {
        return dataStoreApi.loadUsers().map { it.toEntity() }
    }

    suspend fun storeUsers(users: List<UserEntity>) {
        dataStoreApi.saveUsers(users.map { it.toDto() })
    }
}