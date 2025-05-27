package com.example.jonathan.testmvi.features.users.data.dto

import kotlinx.serialization.Serializable

/**
 * DTO used for DataStore serialization. Mirrors UserEntity fields.
 */
@Serializable
data class UserDto(
    val name: String,
    val age: String
)
