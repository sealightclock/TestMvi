package com.example.jonathan.testmvi.features.users.data.repository

import com.example.jonathan.testmvi.features.users.data.datasource.local.UsersLocalDataSource
import com.example.jonathan.testmvi.features.users.data.dto.UserDataMapper.toDto
import com.example.jonathan.testmvi.features.users.data.dto.UserDataMapper.toEntity
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Repository acts as a bridge between the domain and data layers.
 * Handles conversion between entities and DTOs.
 */
class UsersRepository(private val localDataSource: UsersLocalDataSource) {

    suspend fun loadUsers(): List<UserEntity> {
        return localDataSource.getUsers().map { it.toEntity() }
    }

    suspend fun saveUsers(users: List<UserEntity>) {
        localDataSource.storeUsers(users.map { it.toDto() })
    }
}
