package com.example.jonathan.testmvi.features.users.presentation.state

import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Represents the state of the users screen: Input values + user list +
 */
data class UsersState(
    // Input data (used for form fields)
    val inputName: String = "",
    val inputAge: String = "0", // String avoids crash from invalid input

    // List of all created users
    val createdUsers: List<UserEntity> = emptyList(),

    // UI state
    val isLoading: Boolean = false,
)
