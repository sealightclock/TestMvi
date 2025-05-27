package com.example.jonathan.testmvi.features.users.domain.usecase

import com.example.jonathan.testmvi.features.users.data.repository.UsersRepository
import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Stores users to the local DataStore.
 */
class StoreUsersToLocalUseCase(private val repository: UsersRepository) {
    suspend operator fun invoke(users: List<UserEntity>) = repository.saveUsers(users)
}
