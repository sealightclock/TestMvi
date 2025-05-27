package com.example.jonathan.testmvi.features.users.data.repository

import com.example.jonathan.testmvi.features.users.data.datasource.local.UsersLocalDataSource
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Repository serves as the single source of truth for the ViewModel.
 */
class UsersRepository(private val localDataSource: UsersLocalDataSource) {

    suspend fun loadUsers(): List<UserEntity> {
        return localDataSource.getUsers()
    }

    suspend fun saveUsers(users: List<UserEntity>) {
        localDataSource.storeUsers(users)
    }
}
