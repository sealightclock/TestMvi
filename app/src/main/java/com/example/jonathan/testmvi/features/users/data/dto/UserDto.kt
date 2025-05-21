package com.example.jonathan.testmvi.features.users.data.dto

import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity
import kotlinx.serialization.Serializable

/**
 * DTO used for DataStore serialization. Mirrors UserEntity fields.
 */
@Serializable
data class UserDto(
    val name: String,
    val age: String
)

fun UserDto.toEntity() = UserEntity(name = name, age = age.toString())
fun UserEntity.toDto() = UserDto(name = name, age = age)
