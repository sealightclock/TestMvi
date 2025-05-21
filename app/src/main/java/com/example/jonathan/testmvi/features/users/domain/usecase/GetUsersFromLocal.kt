package com.example.jonathan.testmvi.features.users.domain.usecase

import com.example.jonathan.testmvi.features.users.data.repository.UsersRepository
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Loads users from the local DataStore.
 */
class GetUsersFromLocal(private val repository: UsersRepository) {
    suspend operator fun invoke(): List<UserEntity> = repository.loadUsers()
}
