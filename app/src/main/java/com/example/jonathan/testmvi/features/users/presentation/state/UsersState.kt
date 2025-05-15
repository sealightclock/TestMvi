package com.example.jonathan.testmvi.features.users.presentation.state

import com.example.jonathan.testmvi.features.users.domain.entity.UserEntity

/**
 * Represents the state of the users screen: Input values + UI state + user list.
 */
data class UsersState(
    // Input data (used for form fields)
    val name: String = "",
    val age: String = "0", // String avoids crash from invalid input

    // UI state
    val isLoading: Boolean = false,
    val error: String? = null,

    // List of all created users
    val users: List<UserEntity> = emptyList()
)
