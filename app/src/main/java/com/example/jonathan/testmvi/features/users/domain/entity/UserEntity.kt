package com.example.jonathan.testmvi.features.users.domain.entity

/**
 * Represents a single user in the domain layer.
 * This entity can later be reused for business logic, storage, etc.
 */
data class UserEntity(
    val name: String,
    val age: String
)
