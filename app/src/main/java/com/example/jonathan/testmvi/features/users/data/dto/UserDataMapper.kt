package com.example.jonathan.testmvi.features.users.data.dto

import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Responsible for converting between DTOs and domain entities.
 */
object UserDataMapper {
    fun UserDto.toEntity() = UserEntity(name = name, age = age.toString())
    fun UserEntity.toDto() = UserDto(name = name, age = age)
}
